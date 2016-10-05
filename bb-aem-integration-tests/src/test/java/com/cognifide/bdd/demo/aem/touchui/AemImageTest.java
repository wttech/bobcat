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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.ImageComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.cognifide.qa.bb.aem.touch.data.pages.Pages;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemImageTest {

  private static final String CONFIGURATION = "Image - Update&Read";

  private static final String COMPONENT_NAME = "Image";
  public static final String IMAGE_PATH = "cover.jpg";
  public static final String IMAGE_TITLE = "TestTitle";
  public static final String IMAGE_ALT_TEXT = "TestAlt";
  public static final String IMAGE_LINK = "/content/geometrixx";
  public static final String IMAGE_DESCRIPTION = "TestDescription";

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
    parsys = pages.getParsys(CONFIGURATION);
    assertThat("Page has not loaded", page.isLoaded(), is(true));
    assertTrue(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
  }

  @Test
  public void configureImageTest() {
    page.configureComponent(parsys, COMPONENT_NAME, COMPONENT_NAME);

    assertImageComponent();
  }

  @Test
  public void configureImageConfiguredBySingleFieldsTest() {
    page.getParsys(parsys).getComponent(COMPONENT_NAME)
        .openDialog()
        .switchTab("Image")
        .setField("Image asset", FieldType.IMAGE.name(), IMAGE_PATH)
        .setField("Title", FieldType.TEXTFIELD.name(), IMAGE_TITLE)
        .setField("Alt text", FieldType.TEXTFIELD.name(), IMAGE_ALT_TEXT)
        .setField("Link to", FieldType.PATHBROWSER.name(), IMAGE_LINK)
        .setField("Description", FieldType.TEXTFIELD.name(), IMAGE_DESCRIPTION)
        .confirm();

    assertImageComponent();
  }

  private void assertImageComponent() {
    ImageComponent component =
        (ImageComponent) page.getContent(components.getClazz(COMPONENT_NAME));

    assertThat("Wrong image path", component.getImagePath(), endsWith(IMAGE_PATH));
    assertThat("Wrong image title", IMAGE_TITLE, is(component.getTitle()));
    assertThat("Wrong image alt", IMAGE_ALT_TEXT, is(component.getAltText()));
    assertThat("Wrong image link to", component.getLinkTo(), endsWith(IMAGE_LINK + ".html"));
    assertThat("Wrong image description", IMAGE_DESCRIPTION, is(component.getDescription()));
  }
}
