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
}
