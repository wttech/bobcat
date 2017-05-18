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
package com.cognifide.qa.bb.aem.touch.pageobjects;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

import org.openqa.selenium.By;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Class representing author loading page visual effects.
 */
@PageObject
public class AuthorLoader {

  private static final By LOADER_LOCATOR =
      By.cssSelector(".coral-Modal-backdrop .coral-Wait.coral-Wait--center.coral-Wait--large");

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Verifies if loader is hidden.
   */
  public void verifyIsHidden() {
    verifyIsHidden(Timeouts.MEDIUM);
  }

  /**
   * Verifies if loader is hidden before given timeout
   */
  public void verifyIsHidden(int timeout) {
    bobcatWait.verify(invisibilityOfElementLocated(LOADER_LOCATOR), timeout);
  }
}
