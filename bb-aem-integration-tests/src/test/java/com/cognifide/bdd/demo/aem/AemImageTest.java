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
package com.cognifide.bdd.demo.aem;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.product.MandelbrotPage;
import com.cognifide.bdd.demo.po.summer.ImageComponent;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.image.AemImage;
import com.cognifide.qa.bb.aem.ui.AemContentFinder;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemImageTest {

  private static final String IMG_NAME = "ai.jpeg";

  private static final String IMAGE_URL = "/content/dam/geometrixx-media/articles/ai.jpeg";

  private static final int OPENING_PAGE_TIMEOUT_IN_SECONDS = 200;

  private ImageComponent imageComponent;

  private AemImage aemImageField;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private ReportEntryLogger reportEntryLogger;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private MandelbrotPage mandelbrotPage;

  @Inject
  private AemContentFinder contentFinder;

  @Before
  public void setup() {
    aemLogin.authorLogin();
    boolean pageOpened = mandelbrotPage.openPageWithRefresh(OPENING_PAGE_TIMEOUT_IN_SECONDS);
    if (!pageOpened) {
      throw new IllegalStateException("Square page is not displayed");
    }

    imageComponent = mandelbrotPage.getImageComponent();
    aemImageField = openDialogAndWaitForAemImageField(imageComponent);
    clearCurrentImageIfNecessary(aemImageField);
    reportEntryLogger.info("test setup completed");
  }

  @Category(SmokeTests.class)
  @Test
  public void shouldInsertImageWithDragDrop() {
    contentFinder.search(IMG_NAME);
    Draggable elementByName = contentFinder.getElementByName(IMG_NAME);
    imageComponent.insert(elementByName);
    assertTrue(imageComponent.getInfo().endsWith(IMAGE_URL));
  }

  @Test
  public void shouldSetImageValueByJavaScript() {
    contentFinder.search(IMG_NAME);
    aemImageField.setValue(IMG_NAME);
    assertThat(aemImageField.getImageInfo()).endsWith(IMAGE_URL);
  }

  private AemImage openDialogAndWaitForAemImageField(ImageComponent component) {
    component.getDialog().openByContextMenu();
    AemImage result =
        bobcatWait.withTimeout(Timeouts.BIG).until(input -> component.getImageField());
    assertNotNull("Aem image field should be present", result);
    return result;
  }

  private void clearCurrentImageIfNecessary(AemImage aemImage) {
    if (aemImage.hasImageSet()) {
      aemImage.clear();
    }
    if (aemImage.hasImageSet()) {
      throw new IllegalStateException("Image is still present");
    }
  }
}
