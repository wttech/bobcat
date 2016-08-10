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
package com.cognifide.qa.bb.aem.dialog.classic.field.image;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.aem.dialog.classic.field.Configurable;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;

/**
 * Represents an image field in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("image")
public class AemImage implements Configurable {

  private static final String DROP_AN_IMAGE_XPATH = "//div[contains(text(), 'Drop an image')]";

  @FindBy(xpath = "//button[text()='Clear']")
  private WebElement clearButton;

  @FindBy(css = ".x-panel-body")
  private WebElement dropArea;

  @FindBy(css = ".cq-image-icon-info")
  private WebElement infoButton;

  @FindBy(css = ".x-tip .x-tip-body")
  @Global
  private WebElement tip;

  @Inject
  private DragAndDropFactory factory;

  @CurrentFrame
  private FramePath framePath;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private AemImageSetterHelper aemImageSetterHelper;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  /**
   * Checks if there is an image set in image field.
   *
   * @return true if image is set.
   */
  public boolean hasImageSet() {
    return !StringUtils.contains(currentScope.getAttribute(HtmlTags.Attributes.CLASS), "cq-upload-hint");
  }

  /**
   * Clicks the "Clear" button and waits until the image is replaced by default/blank image and
   * message.
   *
   * @return This instance.
   */
  public AemImage clear() {
    waitForToolbarEnabled();
    clearButton.click();
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(
        ExpectedConditions.visibilityOfElementLocated(By.xpath(DROP_AN_IMAGE_XPATH)));
    return this;
  }

  /**
   * Clicks the info button in the image dialog, reads its contents to find referenced URL.
   *
   * @return Image URL address.
   */
  public String getImageInfo() {
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(infoButton));
    infoButton.click();
    return tip.findElement(By.xpath(".//a")).getAttribute(HtmlTags.Attributes.HREF);
  }

  /**
   * Drags image from content finder to image dialog's drop area.
   *
   * @param element WebElement representing image from content finder.
   * @return This instance.
   */
  public AemImage insert(Draggable element) {
    Droppable dropabble = factory.createDroppable(dropArea, framePath);
    element.dropTo(dropabble);
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(
        ExpectedConditions.invisibilityOfElementLocated(By.xpath(DROP_AN_IMAGE_XPATH)));
    return this;
  }

  @Override
  public void setValue(String value) {
    aemImageSetterHelper.setValue(value, currentScope);
  }

  private void waitForToolbarEnabled() {
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(input -> !clearButton
        .findElement(By.xpath("./preceding::table[contains(@class, 'x-btn-text-icon')]"))
        .getAttribute(HtmlTags.Attributes.CLASS).contains("x-item-disabled"));
  }
}
