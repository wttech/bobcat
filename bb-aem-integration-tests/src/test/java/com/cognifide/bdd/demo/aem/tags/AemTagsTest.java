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
package com.cognifide.bdd.demo.aem.tags;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.press.PressPage;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.tags.AemTagItem;
import com.cognifide.qa.bb.aem.dialog.classic.field.tags.AemTags;
import com.cognifide.qa.bb.aem.dialog.classic.field.tags.DeniedTagException;
import com.cognifide.qa.bb.aem.ui.sidekick.AemSidekick;
import com.cognifide.qa.bb.aem.ui.sidekick.PageOperation;
import com.cognifide.qa.bb.aem.ui.sidekick.SidekickTab;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemTagsTest {

  @Inject
  private PressPage pressPage;

  @Inject
  private AemLogin aemLogin;

  private AemTags tags;

  @Before
  public void before() {
    aemLogin.authorLogin();
    pressPage.open();
    assertTrue(pressPage.isDisplayed());
    tags = getTags();
  }

  @Test
  public void testAddNewTag() throws DeniedTagException {
    tags.addTag("", "Bobcat/Test/My Tag");
    assertThat(tags.getTagItem("My Tag"), is(notNullValue()));

    tags.addTag(null, "Bobcat/Test/My Tag 2");
    assertThat(tags.getTagItem("My Tag 2"), is(notNullValue()));
  }

  @Test(expected = DeniedTagException.class)
  public void shouldThrowExceptionIfNamespaceNotValid() throws DeniedTagException {
    tags.addTag("non-existingspace", "Test/My Tag 2");
    fail("Should not reach this code");
  }

  @Test(expected = DeniedTagException.class)
  public void shouldThrowExceptionIfTagNameNotValid() throws DeniedTagException {
    tags.addTag("", "bobcat:invalid-name");
    fail("Should not reach this code");
  }

  @Test
  public void testGetTag() throws DeniedTagException {
    tags.addTag("Bobcat", "Test/My Tag");
    assertThat(tags.getTagItem("My Tag"), is(notNullValue()));
    assertThat(tags.getTagItem("my-tag"), is(nullValue()));

    assertThat(tags.getTagItem("Test/My Tag"), is(notNullValue()));
    assertThat(tags.getTagItem("test/my-tag"), is(nullValue()));

    assertThat(tags.getTagItem("Bobcat:Test/My Tag"), is(notNullValue()));
    assertThat(tags.getTagItem("bobcat:test/my-tag"), is(nullValue()));

    tags.clear();
  }

  @Test
  public void testClear() throws DeniedTagException {
    tags.addTag("Bobcat", "Other Tags/My Tag");
    tags.addTag("Bobcat", "Other Tags/My Tag 2");
    tags.addTag("Bobcat", "Other Tags/My Tag 3");
    tags.clear();
    assertThat(tags.size(), is(0));
  }

  @Test
  public void testRemoveTag() throws DeniedTagException {
    tags.addTag("Bobcat", "Other Tags/My Tag 3");
    tags.removeTag("My Tag 3");
    assertThat(tags.getTagItem("Other Tags/My Tag 3"), is(nullValue()));

    tags.addTag("Bobcat", "Other Tags/My Tag 3");
    tags.removeTag("Other Tags/My Tag 3");
    assertThat(tags.getTagItem("Other Tags/My Tag 3"), is(nullValue()));

    tags.addTag("Bobcat", "Other Tags/My Tag 3");
    tags.removeTag("Bobcat:Other Tags/My Tag 3");
    assertThat(tags.getTagItem("Other Tags/My Tag 3"), is(nullValue()));
  }

  @Test
  public void testIteration() throws DeniedTagException {
    String[] namespaces = {"Bobcat", "Marketing"};
    String[] paths = {"Test/", "Interest/"};
    String[] names = {"My Tag 2", "Investor"};

    for (int i = 0; i < namespaces.length; i++) {
      tags.addTag(namespaces[i], paths[i] + names[i]);
    }

    Iterator<AemTagItem> iterator = tags.iterator();
    int index = 0;
    while (iterator.hasNext()) {
      AemTagItem tagItem = iterator.next();

      String expectedName = names[index++];
      String actualName = tagItem.getTagName();
      assertThat(actualName, is(expectedName));
    }
  }

  private AemTags getTags() {
    AemSidekick sidekick = pressPage.getSidekick();
    sidekick.clickTab(SidekickTab.PAGE).clickOperation(PageOperation.PAGE_PROPERTIES);
    return pressPage.getPageProperties().getTags();
  }
}
