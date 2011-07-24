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

import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.api.batch.AbstractLanguageSensorTest;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.plugins.drools.language.Drools;
import org.sonar.plugins.drools.resources.DroolsFile;

/**
 * Test for drools source importer.
 *
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsSourceImporterTest extends AbstractLanguageSensorTest<Drools> {
  
  @Test
  public void testAnalyse() throws Exception {
    File pomFile = new File("projects/simple/pom.xml");
    final Project project = loadProjectFromPom(pomFile);
    project.getPom().getProperties().put(DroolsPlugin.SOURCE_DIRECTORY, "src/main/rules");
    SensorContext context = analyse(new DroolsSourceImporter(project), project);
    File rule = new File("projects/simple/src/main/rules/org/sonarsource/tests/simple/hello.drl");
    List<File> sourceDirs = new ArrayList<File>();
    sourceDirs.add(new File("projects/simple/src/main/rules"));
    DroolsFile resource = DroolsFile.fromIOFile(rule, false);
    String source = FileUtils.readFileToString(rule, project.getFileSystem().getSourceCharset().name());
    verify(context).saveSource(resource, source);
  }

  @Override
  protected Drools getLanguage() {
    return Drools.INSTANCE;
  }

}
