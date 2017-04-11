/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.loadable;

/**
 * Implementations of this are meant to provide logic for determining whether class that have called
 * a {@link org.openqa.selenium.WebElement} instance is applicable for Loadable Conditions Chain
 * execution
 */
public interface LoadableProcessorFilter {

  /**
   *
   * @param clazz class that calls the {@link org.openqa.selenium.WebElement} instance
   * @return true, when the class applies for run chain loadable conditions. Typically this method
   *         should determine whether the clazz is a tesr runner class or not
   */
  boolean isApplicable(Class clazz);
}
