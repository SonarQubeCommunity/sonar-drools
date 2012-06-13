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
