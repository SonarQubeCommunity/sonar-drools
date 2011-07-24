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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.api.batch.AbstractLanguageSensorTest;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.MockRuleFinder;
import org.sonar.api.rules.Violation;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.plugins.drools.language.Drools;
import org.sonar.plugins.drools.rules.DroolsRuleRepository;

/**
 * Test for drools language sensor.
 * 
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsSensorTest extends AbstractLanguageSensorTest<Drools> {

  @Test
  public void testAnalyseSingleDrlRuleCode() throws Exception {
    File pomFile = new File("projects/simple/pom.xml");
    final Project project = loadProjectFromPom(pomFile);
    project.getPom().getProperties().put(DroolsPlugin.SOURCE_DIRECTORY, "src/main/rules");
    SensorContext context = analyse(new DroolsSensor(new MockRuleFinder(new DroolsRuleRepository(new XMLRuleParser()))), project);
    System.out.println(context);    
  }

  @Test @Ignore
  public void testAnalyseVerifierTests() throws Exception {
    File pomFile = new File("projects/verifier/pom.xml");
    final Project project = loadProjectFromPom(pomFile);
    project.getPom().getProperties().put(DroolsPlugin.SOURCE_DIRECTORY, "src/main/rules");
    SensorContext context = analyse(new DroolsSensor(new MockRuleFinder(new DroolsRuleRepository(new XMLRuleParser()))), project);
    System.out.println(context);
  }

  @Override
  protected Drools getLanguage() {
    return Drools.INSTANCE;
  }

}
