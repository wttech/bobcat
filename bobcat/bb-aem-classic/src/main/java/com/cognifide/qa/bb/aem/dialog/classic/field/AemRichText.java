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
package com.cognifide.qa.bb.aem.dialog.classic.field;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Richtext field in a AemDialog.
 */
@PageObject
@DialogComponent("rich text")
public class AemRichText implements Configurable {

  /**
   * Locator of richtext's toolbar.
   */
  public static final String RT_TOOLBAR = "div.x-html-editor-tb";

  /**
   * Locator of buttons in richtext's toolbar.
   */
  public static final String RT_BUTTON_CSS = RT_TOOLBAR + " button.x-edit-";

  private static final String CLASS_ATTRIBUTE = "class";

  private static final String SELECTED_BUTTON = "div.x-html-editor-wrap table.x-btn-pressed button";

  /**
   * Button class name
   */
  private static final String BUTTON_CLASS_NAME = "x-btn-icon";

  /**
   * "disabled" class value
   */
  private static final String DISABLED_CLASS_NAME = "disabled";

  /**
   * "innerHTML" attribute
   */
  private static final String INNER_HTML_ATTRIBUTE = "innerHTML";

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private FrameSwitcher frameSwitcher;

  @Inject
  private WebDriver webDriver;

  @Inject
  private Actions actions;

  @Inject
  private BobcatWait bobcatWait;

  private String frameName;

  /**
   * Clicks the button indicated by the parameter.
   *
   * @param button button
   * @return This instance.
   */
  @Frame("$cq")
  public AemRichText click(final RtButton button) {
    if (!buttonSelected(button)) {
      bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
        String valueBefore = getTextAreaInnerHtml();
        enableRichTextIfDisabled();
        webDriver.findElement(By.cssSelector(RT_BUTTON_CSS + button.getCss())).click();
        return !valueBefore.equals(getTextAreaInnerHtml());
      }, 2);
    }
    return this;
  }

  /**
   * Types the provided text into richtext's text area.
   *
   * @param text text
   * @return This instance.
   */
  public AemRichText type(CharSequence text) {
    switchToTextArea();
    try {
      actions.sendKeys(text).perform();
      return this;
    } finally {
      frameSwitcher.switchBack();
    }
  }

  /**
   * Clears the content of richtext's text area.
   *
   * @return This instance.
   */
  public AemRichText clear() {
    switchToTextArea();
    try {
      bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
        actions.sendKeys(Keys.chord(Keys.CONTROL, "a")).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).
            perform();
        WebElement activeElement = webDriver.switchTo().activeElement();
        String text = activeElement.getText();
        return text.isEmpty();
      }, 2);
      return this;
    } finally {
      frameSwitcher.switchBack();
    }
  }

  /**
   * Types the provided text into richtext's text area and then types "enter".
   *
   * @param text text
   * @return This instance.
   */
  public AemRichText typeLine(CharSequence text) {
    return type(text).typeNewLine();
  }

  /**
   * Enters new line sign into text area.
   *
   * @return This instance.
   */
  public AemRichText typeNewLine() {
    return type("\n");
  }

  /**
   * Selects all text from the text area.
   *
   * @return This instance.
   */
  public AemRichText selectAll() {
    switchToTextArea();
    try {
      actions.sendKeys(Keys.chord(Keys.CONTROL, "a")).perform();
      return this;
    } finally {
      frameSwitcher.switchBack();
    }
  }

  /**
   * Sets the cursor at the beginning of the current line.
   *
   * @return This instance.
   */
  public AemRichText setCursorAtStartingPos() {
    return type(Keys.HOME);
  }

  /**
   * Sets the cursor at the end of the current line.
   *
   * @return This instance.
   */
  public AemRichText setCursorAtEndPos() {
    return type(Keys.END);
  }

  /**
   * Reads whole text from the text area and returns it.
   *
   * @return Text from the text area.
   */
  public String read() {
    switchToTextArea();
    try {
      return webDriver.findElement(By.xpath(".//body")).getText();
    } finally {
      frameSwitcher.switchBack();
    }
  }

  /**
   * Selects text in the text area between indices provided as parameters.
   *
   * @param startPos the beginning position
   * @param endPos   the ending position
   * @return This instance.
   */
  public AemRichText selectText(int startPos, int endPos) {
    switchToTextArea();
    try {
      actions.sendKeys(Keys.chord(Keys.CONTROL, Keys.HOME)).perform();
      for (int i = 0; i < startPos; i++) {
        actions.sendKeys(Keys.ARROW_RIGHT).perform();
      }
      actions.keyDown(Keys.SHIFT);
      for (int i = 0; i < endPos - startPos; i++) {
        actions.sendKeys(Keys.ARROW_RIGHT);
      }
      actions.keyUp(Keys.SHIFT).perform();
      return this;
    } finally {
      frameSwitcher.switchBack();
    }
  }

  /**
   * @return Reads and returns contents of the richtext's text area, with HTML formatting.
   */
  public String getInnerHTML() {
    switchToTextArea();
    try {
      return webDriver.findElement(By.xpath(".//body")).getAttribute(INNER_HTML_ATTRIBUTE);
    } finally {
      frameSwitcher.switchBack();
    }
  }

  @Override
  public void setValue(String value) {
    clear().type(value);
  }

  private void switchToTextArea() {
    boolean isRichtextDisabled = isRichtextDisabled();
    if (StringUtils.isEmpty(frameName)) {
      frameName = currentScope.findElement(By.tagName("iframe")).getAttribute("name");
    }
    frameSwitcher.switchTo("/$cq/" + frameName);
    if (isRichtextDisabled) {
      webDriver.switchTo().activeElement().click();
    }
  }

  private String getTextAreaInnerHtml() {
    switchToTextArea();
    String value = webDriver.switchTo().activeElement().getAttribute(INNER_HTML_ATTRIBUTE);
    frameSwitcher.switchBack();
    return value;
  }

  private boolean buttonSelected(RtButton button) {
    return webDriver.findElement(By.cssSelector(SELECTED_BUTTON)).getAttribute(CLASS_ATTRIBUTE)
        .contains(button.getCss());
  }

  private void enableRichTextIfDisabled() {
    if (isRichtextDisabled()) {
      bobcatWait.withTimeout(Timeouts.SMALL).until(driver -> {
        type(StringUtils.EMPTY);
        return !isRichtextDisabled();
      }, 1);
    }
  }

  private boolean isRichtextDisabled() {
    return currentScope.findElement(By.className(BUTTON_CLASS_NAME)).getAttribute(CLASS_ATTRIBUTE)
        .contains(DISABLED_CLASS_NAME);
  }
}
