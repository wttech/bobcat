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

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import java.util.Map;
import java.util.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Implementation of {@link ComponentToolbar} working with AEM 6.4
 */
@PageObject(css = "#EditableToolbar")
public class ComponentToolbarImpl implements ComponentToolbar {

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private Map<String,ToolbarOption> toolbarOptions;

  @Inject
  @CurrentScope
  private WebElement toolbar;

  @Override
  public void clickOption(String option) {
    if(toolbarOptions.keySet().contains(option)) {
      toolbar.findElement(toolbarOptions.get(option).getLocator()).click();
    } else {
        throw new NoSuchElementException("Option: " + option + " was not found");
    }
  }

  @Override
  public void clickOption(ToolbarOption option) {
    toolbar.findElement(option.getLocator()).click();
  }

  @Override
  public void verifyIsDisplayed() {
    bobcatWait.withTimeout(Timeouts.SMALL).until(ExpectedConditions.visibilityOf(toolbar));
  }
}
