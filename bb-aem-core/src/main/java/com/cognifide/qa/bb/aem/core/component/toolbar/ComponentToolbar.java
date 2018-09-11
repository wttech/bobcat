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
package com.cognifide.qa.bb.aem.core.component.toolbar;

/**
 * Interface for implementations of ocmponent toolbar
 */
public interface ComponentToolbar {

  /**
   * Method finds given toolbar option and performs click action on it.
   *
   * @param option option which will be clicked.
   */
  public void clickOption(String option);

  /**
   * Method finds given toolbar option (which was not bind to toolbar options)
   * and performs click action on it.
   *
   * @param option option which will be clicked.
   */
  public void clickOption(ToolbarOption option);

  /**
   * Method verifies if the component toolbar is visible.
   */
  public void verifyIsDisplayed();

}
