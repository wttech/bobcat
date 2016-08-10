/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.touch.pageobjects.touchui;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

import org.openqa.selenium.By;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.google.inject.Inject;

/**
 * Class representing author loading page visual effects.
 */
@PageObject
public class AuthorLoader {

  private static final By LOADER_LOCATOR = By.cssSelector( //
      ".coral-Modal-backdrop .coral-Wait.coral-Wait--center.coral-Wait--large");

  @Inject
  private Conditions conditions;

  /**
   * Method verifies if loading visual effect is hidden.
   */
  public void verifyIsHidden() {
    conditions.verify(invisibilityOfElementLocated(LOADER_LOCATOR), Timeouts.MEDIUM);
  }
}
