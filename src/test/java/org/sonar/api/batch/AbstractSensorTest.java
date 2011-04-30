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
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
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
public abstract class AbstractSensorTest<L extends Language> {

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
    SensorContext context = mock(SensorContext.class);
    sensor.analyse(project, context);
    return context;
  }
}
