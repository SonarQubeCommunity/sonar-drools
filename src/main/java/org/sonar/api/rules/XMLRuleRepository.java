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
