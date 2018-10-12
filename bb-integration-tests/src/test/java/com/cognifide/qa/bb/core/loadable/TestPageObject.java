/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.core.loadable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.condition.impl.VisibilityCondition;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class TestPageObject {

  @Inject
  private WebDriver webDriver;

  @FindBy(css = "input[name='1']")
  @LoadableComponent(condClass = TestCondition.class, delay = 3, timeout = 3)
  private InputElementWrapper inputElement1;

  @FindBy(css = "input[name='2']")
  @LoadableComponent(condClass = TestCondition.class, delay = 8, timeout = 8)
  private InputElementWrapper inputElement2;

  @FindBy(tagName = "invalidTag")
  @LoadableComponent(condClass = VisibilityCondition.class, delay = 5, timeout = 5)
  private WebElement invalidElement;

  public void open() {
    webDriver.get(PageUtils.buildTestPageUrl(LoadableComponentsTest.class));
  }

  public void sendKeysWithSuccessToFirstSubj() {
    inputElement1.sendKeys("core");
  }

  public void sendKeysWithSuccessToSecondSubj() {
    inputElement2.sendKeys("core");
  }

  public void sendKeysWithFailure() {
    invalidElement.sendKeys("core");
  }
}
