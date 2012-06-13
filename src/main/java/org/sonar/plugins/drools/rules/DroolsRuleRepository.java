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