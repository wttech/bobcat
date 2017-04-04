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
package com.cognifide.qa.bb.loadable.condition;

import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CommonMonitoredMethods implements MonitoredMethodProvider {

  /**
   * All methods form {@link WebElement} class
   */
  ALL_METHODS {
    @Override
    public List<String> getMethodNames() {
      return Arrays.stream(WebElement.class.getMethods()).map(method -> method.getName())
          .collect(Collectors.toList());
    }
  },

  /**
   * Interactive methods from {@link WebElement} class:
   * <ul>
   *     <li>click</li>
   *     <li>clear</li>
   *     <li>sendKeys</li>
   *     <li>submit</li>
   * </ul>
   */
  INTERACTIVE_METHODS() {
    @Override
    public List<String> getMethodNames() {
      return Arrays.asList("click", "clear", "sendKeys", "submit");
    }
  }
}
