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
