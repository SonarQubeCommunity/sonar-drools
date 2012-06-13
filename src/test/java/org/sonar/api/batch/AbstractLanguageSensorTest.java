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
package org.sonar.api.batch;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;

import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.sonar.api.resources.DefaultProjectFileSystem;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

/**
 * Abstract test for basic language sensor.
 * 
 * @author Jeremie Lagarde
 * @since 0.1
 */
public abstract class AbstractLanguageSensorTest<L extends Language> {

  protected abstract L getLanguage();

  private MavenProject loadPom(File pomFile) throws URISyntaxException {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(pomFile);
      Model model = new MavenXpp3Reader().read(fileReader);
      MavenProject project = new MavenProject(model);
      project.setFile(pomFile);
      project.addCompileSourceRoot(project.getBuild().getSourceDirectory());
      return project;
    } catch (Exception e) {
      throw new SonarException("Failed to read Maven project file : " + pomFile.getPath(), e);
    } finally {
      IOUtils.closeQuietly(fileReader);
    }
  }

  protected Project loadProjectFromPom(File pomFile) throws Exception {
    MavenProject pom = loadPom(pomFile);
    Project project = new Project(pom.getGroupId() + ":" + pom.getArtifactId()).setPom(pom).setConfiguration(
        new MapConfiguration(pom.getProperties()));
    project.setFileSystem(new DefaultProjectFileSystem(project));
    project.setPom(pom);
    project.setLanguageKey(getLanguage().getKey());
    project.setLanguage(getLanguage());

    return project;
  }

  protected SensorContext analyse(Sensor sensor, File pomFile) throws Exception {
    final Project project = loadProjectFromPom(pomFile);
    return analyse(sensor, project);
  }

  protected SensorContext analyse(Sensor sensor, Project project) throws Exception {
    SensorContext context = createMockSensorContext();
    sensor.analyse(project, context);
    return context;
  }

  protected SensorContext createMockSensorContext() {
    return mock(MockSensorContext.class, new CallsRealMethods());
  }  
}
