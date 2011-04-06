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

package org.sonar.plugins.drools.language;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.api.resources.Project;
import org.sonar.plugins.drools.DroolsPlugin;

/**
 * This class defines the Drools language.
 *
 * @author Jérémie Lagarde
 * @since 0.1
 */
public class Drools extends AbstractLanguage {

  /** All the valid files suffixes. */
  private static final String[] DEFAULT_SUFFIXES = { "drl" };

  /** A Drools instance. */
  public static final Drools INSTANCE = new Drools();

  /** The drools language key. */
  public static final String KEY = "drl";

  /** The drools language name */
  private static final String DROOLS_LANGUAGE_NAME = "Drools";

  private String[] fileSuffixes;

  /**
   * Default constructor.
   */
  public Drools() {
    super(KEY, DROOLS_LANGUAGE_NAME);

    fileSuffixes = DEFAULT_SUFFIXES;
  }

  public Drools(Project project) {
    this();

    List<?> extensions = project.getConfiguration().getList(DroolsPlugin.FILE_EXTENSIONS);

    if (extensions != null && !extensions.isEmpty() && !StringUtils.isEmpty((String) extensions.get(0))) {
      fileSuffixes = new String[extensions.size()];
      for (int i = 0; i < extensions.size(); i++) {
        fileSuffixes[i] = extensions.get(i).toString().trim();
      }
    } else {
      fileSuffixes = DEFAULT_SUFFIXES;
    }
  }

  /**
   * Gets the file suffixes.
   *
   * @return the file suffixes
   * @see org.sonar.api.resources.Language#getFileSuffixes()
   */
  public String[] getFileSuffixes() {
    return fileSuffixes;
  }
}
