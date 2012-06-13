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
