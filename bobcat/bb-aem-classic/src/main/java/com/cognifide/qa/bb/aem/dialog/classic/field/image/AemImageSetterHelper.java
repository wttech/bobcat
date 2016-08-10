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

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.ui.AemContentFinder;
import com.cognifide.qa.bb.aem.ui.AemContentFinderTab;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.exceptions.BobcatRuntimeException;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class sets image found in content finder and includes it in given placeholder using JS
 * execution in browser directly. See also imageSetter.js.
 */
@Singleton
@PageObject
class AemImageSetterHelper {

  private static final Logger LOG = LoggerFactory.getLogger(AemImageSetterHelper.class);

  private static final String IMAGE_SETTER_JS_FILENAME = "imageSetter.js";

  private static final long DELAY_BEFORE_RETRY_IN_SECONDS = 1;

  private static String javascriptToExecute;

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private FrameSwitcher frameSwitcher;

  @Inject
  private PageObjectInjector pageObjectInjector;

  void setValue(String imageName, WebElement imagePlaceholder) {
    findImageInContentFinder(imageName);

    String imagePlaceholderId = getImagePlaceholderId(imagePlaceholder);
    executeJavascript(imagePlaceholderId);
  }

  private void findImageInContentFinder(final String imageName) {
    frameSwitcher.switchTo("/");
    AemContentFinder aemContentFinder = pageObjectInjector.inject(AemContentFinder.class);
    LOG.debug("injected content finder: '{}'", aemContentFinder);
    aemContentFinder.search(imageName);
    AemContentFinderTab currentTab = aemContentFinder.getCurrentTab();
    aemContentFinder.changeToListViewIfNeeded(currentTab);
    WebElement image = currentTab.getImageWebElementByName(imageName);
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.elementToBeClickable(image));
    image.click();
    frameSwitcher.switchBack();
  }

  private String getImagePlaceholderId(WebElement imagePlaceholder) {
    By grandfatherSelector = By.xpath("../../.");
    WebElement el = imagePlaceholder.findElement(grandfatherSelector);
    return el.getAttribute("id");
  }

  private void executeJavascript(String imagePlaceholderId) {
    frameSwitcher.switchTo("/");
    JavascriptExecutor js = (JavascriptExecutor) webDriver;
    LOG.debug("executing javascript with argument: '{}'", imagePlaceholderId);

    bobcatWait.withTimeout(Timeouts.MEDIUM).until(driver -> {
      boolean executedSuccessfully = false;
      do {
        try {
          String javaScript = getJavascriptToExecute();
          js.executeScript(javaScript, imagePlaceholderId);
          executedSuccessfully = true;
        } catch (WebDriverException wde) {
          LOG.warn("error while executing JavaScript: '{}'", wde.getMessage(), wde);
        } catch (IOException ioe) {
          String msg = "error while loading JavaScript from file: " + IMAGE_SETTER_JS_FILENAME;
          LOG.error(msg, ioe);
          throw new BobcatRuntimeException(msg);
        }
      } while (!executedSuccessfully);
      return executedSuccessfully;
    }, DELAY_BEFORE_RETRY_IN_SECONDS);
    frameSwitcher.switchBack();
  }

  private static synchronized String getJavascriptToExecute() throws IOException {
    if (javascriptToExecute == null) {
      URL url = Resources.getResource(IMAGE_SETTER_JS_FILENAME);
      javascriptToExecute = Resources.toString(url, Charsets.UTF_8);
    }
    return javascriptToExecute;
  }
}
