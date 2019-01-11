/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.pageobject.active;

public class PageObjectConfigPart {

  private String selectorType;

  private String selector;

  private String pageObjectType;

  private String configName;

  private String className;

  private String name;

  private boolean global;

  public void setSelectorType(String selectorType) {
    this.selectorType = selectorType;
  }

  public void setSelector(String selector) {
    this.selector = selector;
  }

  public void setPageObjectType(String pageObjectType) {
    this.pageObjectType = pageObjectType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSelectorType() {
    return selectorType;
  }

  public String getSelector() {
    return selector;
  }

  public String getPageObjectType() {
    return pageObjectType;
  }

  public boolean isGlobal() {
    return global;
  }

  public void setGlobal(boolean global) {
    this.global = global;
  }

  public String getConfigName() {
    return configName;
  }

  public void setConfigName(String configName) {
    this.configName = configName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
