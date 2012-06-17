/*
 * Sonar Drools Plugin
 * Copyright (C) 2011 Jérémie Lagarde
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.drools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.builder.ResourceType;
import org.drools.io.impl.FileSystemResource;
import org.drools.lang.descr.BaseDescr;
import org.drools.lang.descr.RuleDescr;
import org.drools.verifier.Verifier;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.drools.verifier.components.RuleComponent;
import org.drools.verifier.data.VerifierComponent;
import org.drools.verifier.data.VerifierReport;
import org.drools.verifier.report.components.Severity;
import org.drools.verifier.report.components.VerifierMessageBase;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.drools.language.DrlKeywords;
import org.sonar.plugins.drools.language.Drools;
import org.sonar.plugins.drools.resources.DroolsFile;
import org.sonar.plugins.drools.resources.DroolsPackage;
import org.sonar.plugins.drools.rules.DroolsRuleRepository;
import org.sonar.squid.measures.Metric;
import org.sonar.squid.recognizer.CamelCaseDetector;
import org.sonar.squid.recognizer.CodeRecognizer;
import org.sonar.squid.recognizer.ContainsDetector;
import org.sonar.squid.recognizer.Detector;
import org.sonar.squid.recognizer.EndWithDetector;
import org.sonar.squid.recognizer.KeywordsDetector;
import org.sonar.squid.recognizer.LanguageFootprint;
import org.sonar.squid.text.Source;

/**
 * Drools sensor for standard file metrics.
 * 
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsSensor implements Sensor {

  private static final double CODE_RECOGNIZER_SENSITIVITY = 0.9;

  private final RuleFinder ruleFinder;

  public DroolsSensor(RuleFinder ruleFinder) {
    this.ruleFinder = ruleFinder;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  public boolean shouldExecuteOnProject(Project project) {
    return project.getLanguage().equals(Drools.INSTANCE);
  }

  public void analyse(Project project, SensorContext context) {
    DroolsPlugin.configureSourceDir(project);
    Language drools = new Drools(project);
    ProjectFileSystem fileSystem = project.getFileSystem();
    Map<String, DroolsPackage> packageMap = new HashMap<String, DroolsPackage>();
    VerifierBuilder verifierBuilder = VerifierBuilderFactory.newVerifierBuilder();
    for (File file : fileSystem.getSourceFiles(drools)) {
      Verifier verifier = verifierBuilder.newVerifier();
      try {
        DroolsFile resource = DroolsFile.fromIOFile(file, false);
        Source source = analyseSourceCode(file);
        if (source != null) {
          context.saveMeasure(resource, CoreMetrics.LINES, (double) source.getMeasure(Metric.LINES));
          context.saveMeasure(resource, CoreMetrics.NCLOC, (double) source.getMeasure(Metric.LINES_OF_CODE));
          context.saveMeasure(resource, CoreMetrics.COMMENT_LINES, (double) source.getMeasure(Metric.COMMENT_LINES));
        }
        context.saveMeasure(resource, CoreMetrics.FILES, 1.0);
        context.saveMeasure(resource, CoreMetrics.CLASSES, (double) (resource.getPackageDescr().getRules().size() + resource
            .getPackageDescr().getFunctions().size()));
        packageMap.put(resource.getParent().getKey(), resource.getParent());

        verifier.addResourcesToVerify(new FileSystemResource(file), ResourceType.DRL);
        verifier.fireAnalysis();
        saveViolations(resource, context, verifier.getResult());
      } catch (Exception e) {
        DroolsPlugin.LOG.error("error while verifier analyzing '" + file.getAbsolutePath() + "'", e);
      } finally {
        verifier.dispose();
      }
    }

    for (DroolsPackage droolsPackage : packageMap.values()) {
      context.saveMeasure(droolsPackage, CoreMetrics.PACKAGES, 1.0);
    }
  }

  protected void saveViolations(DroolsFile resource, SensorContext context, VerifierReport report) {
    List<Violation> violations = new ArrayList<Violation>();
    for (Severity severity : Severity.values()) {
      Collection<VerifierMessageBase> messages = report.getBySeverity(severity);
      for (VerifierMessageBase base : messages) {
        Rule rule = findRule(base);
        // ignore violations from report, if rule not activated in Sonar
	    if (rule != null && context.getResource(resource) != null) {
	      int line = getLineNumber(resource, base);
	      Violation violation = Violation.create(rule, resource).setLineId(line).setMessage(base.getMessage());
	      violations.add(violation);          
        }
      }
    }
    context.saveViolations(violations);
  }

  protected int getLineNumber(DroolsFile resource, VerifierMessageBase base) {
    if(base.getFaulty() instanceof VerifierComponent) {
      BaseDescr baseDescr = ((VerifierComponent)base.getFaulty()).getDescr();
      return baseDescr.getLine();
    }
    for (RuleDescr ruleDescr : resource.getPackageDescr().getRules()) {
      if (base.getFaulty() instanceof RuleComponent) {
        RuleComponent ruleComponent = (RuleComponent) base.getFaulty();
        if (ruleComponent.getRuleName().equals(ruleDescr.getName())) {
          return ruleDescr.getLine();
        }
      }
      if(base.getImpactedRules().containsValue(ruleDescr.getName())) {
    	  return ruleDescr.getLine();
      }
    }
    return 1;
  }

  protected Rule findRule(VerifierMessageBase base) {
    return ruleFinder.findByKey(DroolsRuleRepository.REPOSITORY_KEY, "DROOLS_" + base.getMessageType());
  }

  protected static Source analyseSourceCode(File file) {
    Source result = null;
    try {
      result = new Source(new FileReader(file), new CodeRecognizer(CODE_RECOGNIZER_SENSITIVITY, new DrlLanguageFootprint()));
    } catch (FileNotFoundException e) {
      throw new SonarException("Unable to open file '" + file.getAbsolutePath() + "'", e);
    } catch (RuntimeException rEx) {
      DroolsPlugin.LOG.error("error while parsing file '" + file.getAbsolutePath() + "'", rEx);
    }
    return result;
  }

  private static class DrlLanguageFootprint implements LanguageFootprint {

    private static final double CAMEL_CASE_PROBABILITY = 0.5;
    private static final double CONDITIONAL_PROBABILITY = 0.95;
    private static final double DRL_KEYWORDS_PROBABILITY = 0.3;
    private static final double BOOLEAN_OPERATOR_PROBABILITY = 0.7;
    private static final double END_WITH_DETECTOR_PROBABILITY = 0.95;
    private final Set<Detector> detectors = new HashSet<Detector>();

    public DrlLanguageFootprint() {
      detectors.add(new EndWithDetector(END_WITH_DETECTOR_PROBABILITY, '}', ';', '{'));
      detectors.add(new KeywordsDetector(BOOLEAN_OPERATOR_PROBABILITY, "||", "&&"));
      detectors.add(new KeywordsDetector(DRL_KEYWORDS_PROBABILITY, DrlKeywords.toArray()));
      detectors.add(new ContainsDetector(CONDITIONAL_PROBABILITY, "++", "for(", "if(", "while(", "catch(", "switch(", "try{", "else{"));
      detectors.add(new CamelCaseDetector(CAMEL_CASE_PROBABILITY));
    }

    /**
     * @see org.sonar.squid.recognizer.LanguageFootprint#getDetectors()
     */
    public Set<Detector> getDetectors() {
      return detectors;
    }
  }
}
