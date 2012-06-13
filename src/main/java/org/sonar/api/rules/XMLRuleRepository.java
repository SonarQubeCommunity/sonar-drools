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
package org.sonar.api.rules;

import java.util.ArrayList;
import java.util.List;


public abstract class XMLRuleRepository extends RuleRepository {
  
  private XMLRuleParser xmlRuleParser;

  public XMLRuleRepository(String repositoryKey, String languageKey, XMLRuleParser xmlRuleParser) {
    super(repositoryKey, languageKey);
    this.xmlRuleParser = xmlRuleParser;
  }

  @Override
  public final List<Rule> createRules() {
    List<Rule> rules = new ArrayList<Rule>();
    rules.addAll(xmlRuleParser.parse(getClass().getResourceAsStream(getRuleRessource())));
    for (Rule rule : rules) {
      rule.setRepositoryKey(getKey());
    }
    return rules;
  }
  
  protected abstract String getRuleRessource();

}
