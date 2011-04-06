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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.AbstractSourceImporter;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.drools.language.Drools;

/**
 * Import of source files to sonar database.
 *
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsSourceImporter extends AbstractSourceImporter {

  private static final Logger LOG = LoggerFactory.getLogger(DroolsSourceImporter.class);

  public DroolsSourceImporter(Project project) {
    super(new Drools(project));
    DroolsPlugin.configureSourceDir(project);
  }
//
//  @Override
//  protected Resource<?> createResource(java.io.File file, List<java.io.File> sourceDirs, boolean unitTest) {
//    LOG.debug(this.toString()+":" + file.getPath());
//    return File.fromIOFile(file, sourceDirs);
//  }
//
//  @Override
//  public boolean shouldExecuteOnProject(Project project) {
//    return isEnabled(project) && StringUtils.equals(Drools.KEY, project.getLanguageKey());
//  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
