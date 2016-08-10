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
package com.cognifide.bdd.demo.aem.list;

import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.TEST_STRING_ITEM1;
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.TEST_STRING_ITEM2;
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.TEST_STRING_ITEM3;
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.assertProperSequence;
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.getText;
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.typeText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemList;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemListItem;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemListTest {

  @Inject
  private AemListTestHelper helper;

  private AemList list;

  @Before
  public void before() {
    helper.before();
    list = helper.openDialogAndGetList();
  }

  @Test
  public void testSizeMethod() {
    assertThat(list.size(), is(0));

    list.addItem();
    assertThat(list.size(), is(1));

    list.addItem();
    list.addItem();
    assertThat(list.size(), is(3));

  }

  @Test
  public void testClearItems() {
    list.addItem();
    list.addItem();
    list.addItem();
    list.clear();
    assertThat(list.size(), is(0));
  }

  @Test
  public void testGetLastItem() {
    typeText(list.addItem(), TEST_STRING_ITEM1);
    typeText(list.addItem(), TEST_STRING_ITEM2);

    assertThat(getText(list.getLastItem()), is(TEST_STRING_ITEM2));
  }

  @Category(SmokeTests.class)
  @Test
  public void testAddAndGetItem() {
    typeText(list.addItem(), TEST_STRING_ITEM1);
    assertThat(list.size(), is(1));

    typeText(list.addItem(), TEST_STRING_ITEM2);
    assertThat(list.size(), is(2));

    assertThat(getText(list.getItem(0)), is(TEST_STRING_ITEM1));
  }

  @Test
  public void testIteration() {
    typeText(list.addItem(), TEST_STRING_ITEM1);
    typeText(list.addItem(), TEST_STRING_ITEM2);

    assertProperSequence(list, TEST_STRING_ITEM1, TEST_STRING_ITEM2);
  }

  @Category(SmokeTests.class)
  @Test
  public void testRemove() {
    typeText(list.addItem(), TEST_STRING_ITEM1);
    AemListItem secondItem = list.addItem();
    typeText(secondItem, TEST_STRING_ITEM2);
    typeText(list.addItem(), TEST_STRING_ITEM3);

    list.removeItem(secondItem);

    assertProperSequence(list, TEST_STRING_ITEM1, TEST_STRING_ITEM3);
  }

  @Test
  public void testRemoveByIndex() {
    typeText(list.addItem(), TEST_STRING_ITEM1);
    typeText(list.addItem(), TEST_STRING_ITEM2);
    typeText(list.addItem(), TEST_STRING_ITEM3);

    list.removeItem(1);

    assertProperSequence(list, TEST_STRING_ITEM1, TEST_STRING_ITEM3);
  }

}
