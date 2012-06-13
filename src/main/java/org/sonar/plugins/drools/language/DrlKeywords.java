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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class DrlKeywords {

  private static final Set<String> KEYWORDS = new HashSet<String>();

  private static final String[] DROOLS_KEYWORDS = new String[] { "when", "then", "rule", "end", "update", "modify", "retract", "insert",
      "insertLogical", "salience", "import", "expander", "package", "function", "global", "query", "exists", "eval", "agenda-group",
      "lock-on-active", "no-loop", "duration", "->", "not", "auto-focus", "activation-group", "new", "contains", "matches", "excludes",
      "template", "from", "accumulate", "collect", "date-effective", "date-expires", "enabled", "forall", "dialect", "ruleflow-group",
      "modifyRetract", "modifyInsert", "memberOf", "and", "or", "declare" };

  static {
    Collections.addAll(KEYWORDS, DROOLS_KEYWORDS);
  }

  private DrlKeywords() {
  }

  public static Set<String> get() {
    return Collections.unmodifiableSet(KEYWORDS);
  }

  public static String[] toArray() {
    return KEYWORDS.toArray(new String[KEYWORDS.size()]);
  }
}
