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
import com.cognifide.bdd.demo.po.touchui.TitleComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.cognifide.qa.bb.aem.touch.data.pages.Pages;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

import static org.hamcrest.core.Is.is;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemTitleTest {

  private static final String CONFIGURATION = "Title - Update&Read";

  private static final String COMPONENT_NAME = "Title";

  private static final String TAB_NAME = COMPONENT_NAME;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private Pages pages;

  @Inject
  private AuthorPageFactory authorPageFactory;

  @Inject
  private Components components;

  private AuthorPage page;

  private String parsys;

  @Before
  public void setUp() {
    aemLogin.authorLogin();
    page = authorPageFactory.create(pages.getPath(CONFIGURATION));
    page.open();
    org.junit.Assert.assertTrue("Page has not loaded", page.isLoaded());
    parsys = pages.getParsys(CONFIGURATION);
    page.clearParsys(parsys, COMPONENT_NAME);
    org.junit.Assert.assertFalse(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
    page.addComponent(parsys, COMPONENT_NAME);
    org.junit.Assert.assertTrue(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
  }

  @After
  public void tearDown() {
    page.clearParsys(parsys, COMPONENT_NAME);
    org.junit.Assert.assertFalse(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
  }

  @Test
  public void editTitleComponentTest() {
    ComponentConfiguration data = page.configureComponent(parsys,
        COMPONENT_NAME, COMPONENT_NAME.toLowerCase());
    TitleComponent component =
        (TitleComponent) page.getContent(components.getClazz(COMPONENT_NAME));
    org.junit.Assert.assertThat(component.getTitle(), is(
        data.getConfigurationForTab(TAB_NAME).get(0).getValue().toString().toUpperCase()));
  }

  @Test
  public void editTitleComponentConfiguredBySingleFieldsTest() {
    String titleValue = "Feedback1";

    page.getParsys(parsys).getComponent(COMPONENT_NAME)
        .openDialog()
        .switchTab("Title")
        .setField("Title", FieldType.TEXTFIELD.name(), titleValue)
        .confirm();

    TitleComponent component =
        (TitleComponent) page.getContent(components.getClazz(COMPONENT_NAME));
    org.junit.Assert.assertThat(component.getTitle(), is(titleValue.toUpperCase()));
  }
}
