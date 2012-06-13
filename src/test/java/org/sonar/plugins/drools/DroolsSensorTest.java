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

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.api.batch.AbstractLanguageSensorTest;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.MockRuleFinder;
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
