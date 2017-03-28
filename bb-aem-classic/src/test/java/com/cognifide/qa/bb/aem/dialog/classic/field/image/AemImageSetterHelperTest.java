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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.ui.AemContentFinder;
import com.cognifide.qa.bb.aem.ui.AemContentFinderTab;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.provider.selenium.BobcatWebDriverWait;
import com.cognifide.qa.bb.utils.PageObjectInjector;

@RunWith(MockitoJUnitRunner.class)
public class AemImageSetterHelperTest {

  private static final String IMAGE_NAME = "image-name";

  private static final String IMAGE_PLACEHOLDER_ID = "my-object-id";

  private static final String XPATH_TWO_LEVELS_UP = "../../.";

  private static final long DEFAULT_BOBCAT_TIMEOUT = 10;

  @Mock(extraInterfaces = {JavascriptExecutor.class})
  private WebDriver webDriver;

  @Mock
  private WebDriver.Options webDriverOptions;

  @Mock
  private WebDriver.Timeouts webDriverTimeouts;

  @Mock
  private FrameSwitcher frameSwitcher;

  @Mock
  private BobcatWait bobcatWait;

  @Mock
  private WebElement imagePlaceholderWebElement;

  @Mock
  private WebElement imagePlaceholderIdWebElement;

  @Mock
  private PageObjectInjector pageObjectInjector;

  @Mock
  private WebElement clickableWebElement;

  @Mock
  private AemContentFinder aemContentFinder;

  @Mock
  private AemContentFinderTab aemContentFinderTab;

  @InjectMocks
  private final AemImageSetterHelper tested;

  public AemImageSetterHelperTest() {
    this.tested = new AemImageSetterHelper();
  }

  @Before
  public void setUp() throws Exception {
    Mockito.reset(webDriver, webDriverOptions, webDriverTimeouts, frameSwitcher, bobcatWait,
        pageObjectInjector, clickableWebElement, aemContentFinder, aemContentFinderTab);
    configureMockWebElementForImagePlaceholderId(IMAGE_PLACEHOLDER_ID);

    when(webDriver.manage()).thenReturn(webDriverOptions);
    when(webDriverOptions.timeouts()).thenReturn(webDriverTimeouts);

    when(bobcatWait.withTimeout(any(Integer.class))).thenAnswer(
        (Answer<BobcatWebDriverWait>) invocation -> {
          Object[] args = invocation.getArguments();
          return new BobcatWebDriverWait(webDriver, (int) args[0], DEFAULT_BOBCAT_TIMEOUT);
        });
    when(pageObjectInjector.inject(eq(AemContentFinder.class))).thenReturn(aemContentFinder);
    when(aemContentFinder.getCurrentTab()).thenReturn(aemContentFinderTab);
    when(aemContentFinderTab.getImageWebElementByName(eq(IMAGE_NAME)))
        .thenReturn(clickableWebElement);
    when(clickableWebElement.isDisplayed()).thenReturn(true);
    when(clickableWebElement.isEnabled()).thenReturn(true);
  }

  @Test
  public void shouldClickOnceOnFoundWebElement() throws Exception {
    // when
    tested.setValue(IMAGE_NAME, imagePlaceholderWebElement);

    // then
    verify(clickableWebElement, times(1)).click();
  }

  @Test
  public void shouldSearchImagePlaceholderIdTwoLevelsUp() throws Exception {
    // when
    tested.setValue(IMAGE_NAME, imagePlaceholderWebElement);

    // then
    verify(imagePlaceholderWebElement).findElement(eq(By.xpath(XPATH_TWO_LEVELS_UP)));
  }

  @Test
  public void shouldExecuteJavascriptCode() throws Exception {
    // when
    tested.setValue(IMAGE_NAME, imagePlaceholderWebElement);

    // then
    verify((JavascriptExecutor) webDriver)
        .executeScript(any(String.class), eq(IMAGE_PLACEHOLDER_ID));
  }

  @Test
  public void shouldSwitchFrameBeforeExecutingJavascriptAndSwitchBackAfter() throws Exception {
    // given
    InOrder inOrder = inOrder(webDriver, frameSwitcher);

    // when
    tested.setValue(IMAGE_NAME, imagePlaceholderWebElement);

    // then
    inOrder.verify(frameSwitcher).switchTo(eq("/"));
    inOrder.verify((JavascriptExecutor) webDriver)
        .executeScript(any(String.class), eq(IMAGE_PLACEHOLDER_ID));
    inOrder.verify(frameSwitcher).switchBack();
  }

  @Test
  public void shouldSwitchFrameBeforeSearchingImageAndSwitchingItBackBeforeClickingOnFoundElement() {
    // given
    InOrder inOrder = inOrder(frameSwitcher, aemContentFinder, clickableWebElement);

    // when
    tested.setValue(IMAGE_NAME, imagePlaceholderWebElement);

    // then
    inOrder.verify(frameSwitcher).switchTo(eq("/"));
    inOrder.verify(aemContentFinder).search(eq(IMAGE_NAME));
    inOrder.verify(clickableWebElement).click();
    inOrder.verify(frameSwitcher).switchBack();
  }

  private void configureMockWebElementForImagePlaceholderId(String placeholderId) {
    when(imagePlaceholderWebElement
        .findElement(eq(By.xpath(XPATH_TWO_LEVELS_UP)))).thenReturn(imagePlaceholderIdWebElement);
    when(imagePlaceholderIdWebElement
        .getAttribute(eq("id"))).thenReturn(placeholderId);
  }

}
