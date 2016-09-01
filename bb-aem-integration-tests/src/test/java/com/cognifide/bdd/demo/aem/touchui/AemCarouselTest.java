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
package com.cognifide.bdd.demo.aem.touchui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.CarouselComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.cognifide.qa.bb.aem.touch.data.pages.Pages;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.ConfigDialog;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.Multifield;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.*;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemCarouselTest {

  private static final String CONFIGURATION = "Carousel - Update&Read";

  private static final String COMPONENT_NAME = "Carousel";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private AuthorPageFactory authorPageFactory;

  @Inject
  private Pages pages;

  @Inject
  private Components components;

  private AuthorPage page;

  private String parsys;

  @Before
  public void setUp() {
    aemLogin.authorLogin();
    page = authorPageFactory.create(pages.getPath(CONFIGURATION));
    page.open();
    assertTrue("Page has not loaded", page.isLoaded());
    parsys = pages.getParsys(CONFIGURATION);
    page.clearParsys(parsys, COMPONENT_NAME);
    assertFalse(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
    page.addComponent(parsys, COMPONENT_NAME);
    assertTrue(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
  }

  @After
  public void tearDown() {
    page.clearParsys(parsys, COMPONENT_NAME);
    assertFalse(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
  }

  @Test
  public void editCarouselComponentTest() {
    page.configureComponent(parsys, COMPONENT_NAME, COMPONENT_NAME);
    assertCarouselComponent();
  }

  @Test
  public void editCarouselComponentConfiguredBySingleFieldsTest() {
    ConfigDialog dialog = page.getParsys(parsys)
        .getComponent(COMPONENT_NAME)
        .openDialog()
        .switchTab("LIST")
        .setField("Build list using", FieldType.SELECT.name(), "Fixed list");

    Multifield multifield = (Multifield) dialog.getField("Pages", FieldType.MULTIFIELD.name());
    multifield.addField();
    multifield.addField();
    multifield.getItemAtIndex(0)
        .setFieldInMultifield(FieldType.PATHBROWSER.name(), "/content/geometrixx-outdoors");
    multifield.getItemAtIndex(1)
        .setFieldInMultifield(FieldType.PATHBROWSER.name(), "/content/geometrixx-outdoors-mobile");
    dialog.confirm();
    assertCarouselComponent();
  }

  private void assertCarouselComponent() {
    CarouselComponent component =
        (CarouselComponent) page.getContent(components.getClazz(COMPONENT_NAME));

    assertThat(component.getSize(), is(2));
    assertThat(component.getAnchorHref(0), endsWith("/content/geometrixx-outdoors.html"));
    assertThat(component.getAnchorHref(1), endsWith("/content/geometrixx-outdoors-mobile.html"));
  }
}
