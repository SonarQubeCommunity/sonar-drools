/*
 * Sonar Drools Plugin
 * Copyright (C) 2011 Jérémie Lagarde
 * dev@sonar.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonar.plugins.drools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.drools.language.DrlKeywords;
import org.sonar.plugins.drools.language.Drools;
import org.sonar.plugins.drools.resources.DroolsFile;
import org.sonar.plugins.drools.resources.DroolsPackage;
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
  
  public boolean shouldExecuteOnProject(Project project) {
    return project.getLanguage().equals(Drools.INSTANCE);
  }

  public void analyse(Project project, SensorContext context) {
    DroolsPlugin.configureSourceDir(project);
    Language drools = new Drools(project);
    ProjectFileSystem fileSystem = project.getFileSystem();
    Map<String, DroolsPackage> packageMap = new HashMap<String, DroolsPackage>();
    for (File file : fileSystem.getSourceFiles(drools)) {
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
      } catch (Exception e) {
      }
    }
    for (DroolsPackage droolsPackage : packageMap.values()) {
      context.saveMeasure(droolsPackage, CoreMetrics.PACKAGES, 1.0);
    }
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
