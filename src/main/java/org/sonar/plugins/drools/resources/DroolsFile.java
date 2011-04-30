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
      PackageDescr packageDescr = parser.parse(new FileReader(file));
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
