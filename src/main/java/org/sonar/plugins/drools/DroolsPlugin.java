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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.Extension;
import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.resources.Project;
import org.sonar.plugins.drools.language.Drools;
import org.sonar.plugins.drools.language.DroolsCodeColorizerFormat;
import org.sonar.plugins.drools.rules.DefaultDroolsProfile;
import org.sonar.plugins.drools.rules.DroolsRuleRepository;

/**
 * Drools Plugin publishes extensions to sonar engine.
 * 
 * @author Jeremie Lagarde
 * @since 0.1
 */
@Properties({
    @Property(key = DroolsPlugin.FILE_EXTENSIONS, name = "File extensions", description = "List of file extensions that will be scanned.",
        defaultValue = "drl,dsl,rf", global = true, project = true),
    @Property(key = DroolsPlugin.SOURCE_DIRECTORY, name = "Source directory", description = "Source directory that will be scanned.",
        defaultValue = "src/main/rules", global = false, project = true) })
public final class DroolsPlugin implements Plugin {

  public static final Logger LOG = LoggerFactory.getLogger("org.sonar.plugins.drools");

  public static final String FILE_EXTENSIONS = "sonar.drools.fileExtensions";
  private static final String KEY = "sonar-drools-plugin";
  public static final String SOURCE_DIRECTORY = "sonar.drools.sourceDirectory";

  public static void configureSourceDir(Project project) {
    String sourceDir = (String) project.getProperty(SOURCE_DIRECTORY);
    if (sourceDir != null) {
      project.getFileSystem().getSourceDirs().clear();
      project.getFileSystem().addSourceDir(project.getFileSystem().resolvePath(sourceDir));
    } else {
      for (Iterator iterator = project.getPom().getResources().iterator(); iterator.hasNext();) {
        Resource resource = (Resource) iterator.next();
        File resourceDir = project.getFileSystem().resolvePath(resource.getDirectory());
        if ( !project.getFileSystem().getSourceDirs().contains(resourceDir))
          System.out.println(" Config :: Adding " + resourceDir.getAbsolutePath());
          project.getFileSystem().addSourceDir(resourceDir);
      }
    }
  }

  public String getDescription() {
    return getName() + " collects metrics on Drools files, such as lines of code, rule violations ...";
  }

  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();

    // Drools language
    list.add(Drools.class);
    // Source importer
    list.add(DroolsSourceImporter.class);
    // Source Code Colorizer
    list.add(DroolsCodeColorizerFormat.class);
    // Rules repository
    list.add(DroolsRuleRepository.class);
    // Default Profile
    list.add(DefaultDroolsProfile.class);
    // Metrics sensor
    list.add(DroolsSensor.class);

    return list;
  }

  public String getKey() {
    return KEY;
  }

  public String getName() {
    return "Drools";
  }

  @Override
  public String toString() {
    return getKey();
  }
}
