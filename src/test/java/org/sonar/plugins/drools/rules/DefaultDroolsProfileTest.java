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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.sonar.plugins.drools.language.Drools;

import org.sonar.plugins.drools.DroolsPlugin;

import org.sonar.api.profiles.RulesProfile;

import org.junit.Test;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.api.utils.ValidationMessages;

import static org.junit.Assert.fail;

/**
 * Test for drools source importer.
 *
 * @author Jeremie Lagarde
 * @since 0.2
 */
public class DefaultDroolsProfileTest {

  @Test
  public void test() {
    DefaultDroolsProfile profile = new DefaultDroolsProfile(new DroolsRuleRepository(new XMLRuleParser()));
    RulesProfile rulesProfile = profile.createProfile(ValidationMessages.create());
    assertThat(rulesProfile.getActiveRules().size(),is(12));
    assertThat(rulesProfile.getLanguage(),is(Drools.KEY));
  }

}
