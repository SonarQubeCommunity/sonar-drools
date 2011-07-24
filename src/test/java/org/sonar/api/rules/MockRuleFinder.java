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

import java.util.Collection;
import java.util.List;

/**
 * Mock class for RuleFinder component.
 * 
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class MockRuleFinder implements RuleFinder {

  private List<Rule> rules;

  
  public MockRuleFinder(RuleRepository ruleRepository) {
    rules = ruleRepository.createRules();
  }
  
  public Rule findById(int ruleId) {
    throw new UnsupportedOperationException();
  }

  public Rule findByKey(String repositoryKey, String key) {
    for (Rule rule : rules) {
      if (rule.getKey().equals(key)) {
        return rule;
      }
    }
    return null;
  }

  public Rule find(RuleQuery query) {
    throw new UnsupportedOperationException();
  }

  public Collection<Rule> findAll(RuleQuery query) {
    return rules;
  }
}
