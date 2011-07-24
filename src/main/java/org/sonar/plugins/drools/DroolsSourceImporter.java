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
import java.util.List;

import org.sonar.api.batch.AbstractSourceImporter;
import org.sonar.api.resources.Project;
import org.sonar.plugins.drools.language.Drools;
import org.sonar.plugins.drools.resources.DroolsFile;

/**
 * Import of source files to sonar database.
 *
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsSourceImporter extends AbstractSourceImporter {

  public DroolsSourceImporter(Project project) {
    super(new Drools(project));
    DroolsPlugin.configureSourceDir(project);
  }

  @Override
  protected DroolsFile createResource(File file, List<File> sourceDirs, boolean unitTest) {
    return file != null ? DroolsFile.fromIOFile(file, unitTest) : null;
  }


  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
