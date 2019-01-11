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

import java.util.List;

public class PageObjectConfiguration {

  private String selectorType;

  private String selector;

  private List<PageObjectConfigPart> parts;

  private boolean global;

  public boolean isGlobal() {
    return global;
  }

  public void setGlobal(boolean global) {
    this.global = global;
  }

  public String getSelectorType() {
    return selectorType;
  }

  public void setSelectorType(String selectorType) {
    this.selectorType = selectorType;
  }

  public String getSelector() {
    return selector;
  }

  public void setSelector(String selector) {
    this.selector = selector;
  }

  public List<PageObjectConfigPart> getParts() {
    return parts;
  }

  public void setParts(List<PageObjectConfigPart> parts) {
    this.parts = parts;
  }
}
