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

import java.util.List;

import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.Image;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.PathBrowser;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.Textfield;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.ImageComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldConfig;
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
    ComponentConfiguration componentConfig = page.configureComponent(parsys,
        COMPONENT_NAME, COMPONENT_NAME);

    ImageComponent component =
        (ImageComponent) page.getContent(components.getClazz(COMPONENT_NAME));

    List<FieldConfig> configuration = componentConfig.getConfigurationForTab(COMPONENT_NAME);
    assertThat("Wrong image path", component.getImagePath(),
        endsWith(configuration.get(0).getValue().toString()));
    assertThat("Wrong image title", configuration.get(1).getValue(), is(component.getTitle()));
    assertThat("Wrong image alt", configuration.get(2).getValue(), is(component.getAltText()));
    assertThat("Wrong image link to", component.getLinkTo(),
        endsWith(configuration.get(3).getValue().toString() + ".html"));
    assertThat("Wrong image description", configuration.get(4).getValue(),
        is(component.getDescription()));
  }

  @Test
  public void configureImageConfiguredBySingleFieldsTest() {
    String imagePath = "cover";
    String imageTitle = "TestTitle";
    String imageAlt = "TestAlt";
    String imageLink = "/content/geometrixx";
    String imageDescription = "TextDescription";

    page.getParsys(parsys).getComponent(COMPONENT_NAME)
        .openDialog()
        .switchTab("Image")
        .setField("Image asset", Image.class, imagePath)
        .setField("Title", Textfield.class, imageTitle)
        .setField("Alt text", Textfield.class, imageAlt)
        .setField("Link to", PathBrowser.class, imageLink)
        .setField("Description", Textfield.class, imageDescription)
        .confirm();

    ImageComponent component =
        (ImageComponent) page.getContent(components.getClazz(COMPONENT_NAME));

    assertThat("Wrong image path", component.getImagePath(),
        endsWith(imagePath));
    assertThat("Wrong image title", imageTitle, is(component.getTitle()));
    assertThat("Wrong image alt", imageAlt, is(component.getAltText()));
    assertThat("Wrong image link to", component.getLinkTo(),
        endsWith(imageLink + ".html"));
    assertThat("Wrong image description", imageDescription,
        is(component.getDescription()));
  }
}
