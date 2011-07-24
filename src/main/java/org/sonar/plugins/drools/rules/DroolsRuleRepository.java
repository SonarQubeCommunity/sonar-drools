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

import org.sonar.api.rules.XMLRuleParser;
import org.sonar.api.rules.XMLRuleRepository;
import org.sonar.plugins.drools.language.Drools;


/**
 * Drools rule repository.
 *
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsRuleRepository extends XMLRuleRepository {
  
  public final static String REPOSITORY_KEY = "sonar-drools-plugin"; 
  public final static String REPOSITORY_NAME = "Drools Violations"; 
  
  public DroolsRuleRepository(XMLRuleParser xmlRuleParser) {
    super(REPOSITORY_KEY, Drools.KEY, xmlRuleParser);
    setName(REPOSITORY_NAME);
  }

  @Override
  protected String getRuleRessource() {
    return "/org/sonar/plugins/drools/rules/rules.xml";
  }

}