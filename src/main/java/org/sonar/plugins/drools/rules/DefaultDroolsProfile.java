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

package org.sonar.plugins.drools.rules;

import java.util.List;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.drools.language.Drools;

/**
 * Default Drools Profile.
 * It activates by default the  MAJOR, CRITICAL and BLOCKER rules
 * 
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DefaultDroolsProfile extends ProfileDefinition {
  
  private RuleRepository repository;
  
  public DefaultDroolsProfile(DroolsRuleRepository ruleRepository) {
    this.repository = ruleRepository;
  }
  
  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    List<Rule> rules = repository.createRules();
    RulesProfile rulesProfile = RulesProfile.create(DroolsRuleRepository.REPOSITORY_NAME, Drools.KEY);
    for (Rule rule : rules) {
      //if(RulePriority.MAJOR.equals(rule.getPriority()) || RulePriority.CRITICAL.equals(rule.getPriority())  || RulePriority.BLOCKER.equals(rule.getPriority() ))
      rulesProfile.activateRule(rule,null);
    }
    rulesProfile.setDefaultProfile(true);
    return rulesProfile;
  }
}
 
