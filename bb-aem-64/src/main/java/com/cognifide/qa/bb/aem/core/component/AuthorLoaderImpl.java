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
package com.cognifide.qa.bb.aem.core.component;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

import org.openqa.selenium.By;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Implementation of {@link AuthorLoader} for AEM 6.4
 */
@PageObject
public class AuthorLoaderImpl implements AuthorLoader {

  private static final By LOADER_LOCATOR = By.cssSelector( //
      ".coral-Modal-backdrop .coral-Wait.coral-Wait--center.coral-Wait--large");

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void verifyIsHidden() {
    bobcatWait.until(invisibilityOfElementLocated(LOADER_LOCATOR));
  }
}
