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
package com.cognifide.qa.bb.provider.selenium.webdriver;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.frame.FrameSwitcher;

/**
 * Target locator that uses {@link FrameSwitcher}.
 * Used by {@link WebDriverWrapper}
 */
public class BobcatTargetLocator implements TargetLocator {

  private final FrameSwitcher frameSwitcher;

  private final TargetLocator targetLocator;

  /**
   * Constructs TargetLocatorWrapper.
   *
   * @param targetLocator instance of TargetLocator.
   * @param frameSwitcher instance of FrameSwitcher.
   */
  public BobcatTargetLocator(TargetLocator targetLocator, FrameSwitcher frameSwitcher) {
    super();
    this.targetLocator = targetLocator;
    this.frameSwitcher = frameSwitcher;
  }

  /**
   * Switches to frame indicated by index and puts the frame on FrameSwitcher's stack.
   *
   * @param index frame index on the stack.
   */
  @Override
  public WebDriver frame(int index) {
    frameSwitcher.putFramePathOnStack(index);
    return targetLocator.frame(index);
  }

  /**
   * Switches to frame indicated by name and puts the frame on FrameSwitcher's stack.
   *
   * @param nameOrId frame name or id.
   */
  @Override
  public WebDriver frame(String nameOrId) {
    frameSwitcher.putFramePathOnStack(nameOrId);
    return targetLocator.frame(nameOrId);
  }

  /**
   * See parent.
   */
  @Override
  public WebDriver frame(WebElement frameElement) {
    return targetLocator.frame(frameElement);
  }

  /**
   * See parent.
   */
  @Override
  public WebDriver parentFrame() {
    return targetLocator.parentFrame();
  }

  /**
   * See parent.
   */
  @Override
  public WebDriver window(String nameOrHandle) {
    return targetLocator.window(nameOrHandle);
  }

  /**
   * Switches to default frame and puts the frame on FrameSwitcher's stack.
   */
  @Override
  public WebDriver defaultContent() {
    frameSwitcher.putDefaultFramePathOnStack();
    return targetLocator.defaultContent();
  }

  /**
   * See parent.
   */
  @Override
  public WebElement activeElement() {
    return targetLocator.activeElement();
  }

  /**
   * See parent.
   */
  @Override
  public Alert alert() {
    return targetLocator.alert();
  }

}
