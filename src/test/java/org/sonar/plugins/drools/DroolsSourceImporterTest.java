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
