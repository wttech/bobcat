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
package com.cognifide.qa.bb.webelement;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.HasIdentity;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

/**
 * Improves WebElement implementation with custom {@link #sendKeys(CharSequence...)} method
 */
public class BobcatWebElement implements WebElement, Locatable, WrapsElement, HasIdentity {

  private static final int SEND_KEYS_RETRIES = 10;

  private final WebElement element;

  private final Locatable locatable;

  private final List<ConditionContext> conditionContext;

  @Inject
  public BobcatWebElement(@Assisted BobcatWebElementContext context) {
    this.element = context.getWebElement();
    this.locatable = context.getLocatable();
    this.conditionContext = context.getLoadableConditionContext();
  }

  @Override
  public void click() {
    element.click();
  }

  @Override
  public void submit() {
    element.submit();
  }

  /**
   * As there is known selenium
   * <a href="https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/4446">4446</a>
   * bug this method retries sending keys until input field value is valid.
   *
   * @param keysToSend keyboard keys to send
   */
  @Override
  public void sendKeys(CharSequence... keysToSend) {
    String textBefore = getValue();
    if (isKeys(keysToSend[0]) || isUploadField(element)) {
      element.sendKeys(keysToSend);
    } else {
      for (int i = 0; i < SEND_KEYS_RETRIES; i++) {
        element.sendKeys(keysToSend);
        boolean success = StringUtils.endsWith(getValue(), keysToSend[0]);
        if (success) {
          break;
        } else {
          element.clear();
          this.sendKeys(textBefore);
        }
      }
    }
  }

  @Override
  public void clear() {
    element.clear();
  }

  @Override
  public String getTagName() {
    return element.getTagName();
  }

  @Override
  public String getAttribute(String name) {
    return element.getAttribute(name);
  }

  @Override
  public boolean isSelected() {
    return element.isSelected();
  }

  @Override
  public boolean isEnabled() {
    return element.isEnabled();
  }

  @Override
  public String getText() {
    return element.getText();
  }

  @Override
  public List<WebElement> findElements(By by) {
    return element.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return element.findElement(by);
  }

  @Override
  public boolean isDisplayed() {
    return element.isDisplayed();
  }

  @Override
  public Point getLocation() {
    return element.getLocation();
  }

  @Override
  public Dimension getSize() {
    return element.getSize();
  }

  @Override
  public Rectangle getRect() {
    return element.getRect();
  }

  @Override
  public String getCssValue(String propertyName) {
    return element.getCssValue(propertyName);
  }

  @Override
  public Coordinates getCoordinates() {
    return locatable.getCoordinates();
  }

  @Override
  public WebElement getWrappedElement() {
    return element;
  }

  @Override
  public <T> T getScreenshotAs(OutputType<T> target) {
    return element.getScreenshotAs(target);
  }

  @Override
  public String toString() {
    return String.format("%s(%s)", this.getClass().getSimpleName(), getWrappedElement());
  }

  /**
   *
   * @return Conditions defined by {@link LoadableComponent} annotations.
   */
  public List<ConditionContext> getLoadableConditionContext() {
    return conditionContext;
  }

  private boolean isUploadField(WebElement webElement) {
    boolean tagIsInput = "input".equals(webElement.getTagName());
    boolean typeIsFile = "file".equals(webElement.getAttribute("type"));

    return tagIsInput && typeIsFile;
  }

  private boolean isKeys(CharSequence charSequence) {
    return charSequence instanceof Keys;
  }

  private String getValue() {
    return element.getAttribute("value");
  }

  @Override
  public String getId() {
    return UUID.randomUUID().toString();
  }
}
