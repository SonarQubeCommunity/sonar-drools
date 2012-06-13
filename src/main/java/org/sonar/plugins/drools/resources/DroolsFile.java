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
package org.sonar.plugins.drools.resources;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.lang.StringUtils;
import org.drools.compiler.DrlParser;
import org.drools.lang.descr.PackageDescr;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.WildcardPattern;
import org.sonar.plugins.drools.DroolsPlugin;
import org.sonar.plugins.drools.language.Drools;

/**
 * Drools file as sonar resource.
 *
 * @author Jeremie Lagarde
 * @since 0.1
 */
public class DroolsFile extends Resource<DroolsPackage> {

  private String fileName;
  private String longName;
  private String packageKey;
  private boolean unitTest = false;
  private DroolsPackage parent = null;
  private PackageDescr packageDescr = null;

  public DroolsFile(PackageDescr packageDescr, String fileName,  boolean unitTest) {
    super();
    this.setPackageDescr(packageDescr);
    if (StringUtils.isNotBlank(fileName))
      this.fileName = fileName;
    String key;
    if (packageDescr == null) {
      this.packageKey = DroolsPackage.DEFAULT_PACKAGE_NAME;
      this.longName = this.fileName;
      key = new StringBuilder().append(this.packageKey).append(".").append(this.fileName).toString();
    } else {
      parent = new DroolsPackage(packageDescr.getName());
      this.packageKey = parent.getKey();
      key = new StringBuilder().append(this.packageKey).append(".").append(this.fileName).toString();
      this.longName = key;
    }
    setKey(key);
    this.unitTest = unitTest;
  }
  

  public static DroolsFile fromIOFile(File file, boolean unitTest) {
    if (file == null || StringUtils.isBlank(file.getName())) {
      return null;
    }
    DrlParser parser = new DrlParser();
    try {
      PackageDescr packageDescr = parser.parse(true, new FileReader(file));
      return new DroolsFile(packageDescr, file.getName(), unitTest);
    } catch (Exception e) {
      DroolsPlugin.LOG.error("Unable to parse " + file.getName(),e);
      return null;
    }
  }

  @Override
  public String getName() {
    return this.fileName;
  }

  @Override
  public String getLongName() {
    return this.longName;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public Language getLanguage() {
    return Drools.INSTANCE;
  }

  @Override
  public String getScope() {
    return Resource.SCOPE_ENTITY;
  }

  @Override
  public String getQualifier() {
    return unitTest ? Resource.QUALIFIER_UNIT_TEST_CLASS : Resource.QUALIFIER_CLASS;
  }

  @Override
  public DroolsPackage getParent() {
    return parent;
  }

  @Override
  public boolean matchFilePattern(String antPattern) {
    String patternWithoutFileSuffix = StringUtils.substringBeforeLast(antPattern, ".");
    WildcardPattern matcher = WildcardPattern.create(patternWithoutFileSuffix, ".");
    return matcher.match(getKey());
  }


  public void setPackageDescr(PackageDescr packageDescr) {
    this.packageDescr = packageDescr;
  }


  public PackageDescr getPackageDescr() {
    return packageDescr;
  }

}
