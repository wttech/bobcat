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
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.assertProperSequence;
import static com.cognifide.bdd.demo.aem.list.AemListTestHelper.typeText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemList;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemListItem;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemListItemTest {

  @Inject
  private AemListTestHelper helper;

  private AemList list;

  @Before
  public void before() {
    helper.before();
    list = helper.openDialogAndGetList();
  }

  @Test
  public void testMoveUp() {
    typeText(list.addItem(), TEST_STRING_ITEM1);
    AemListItem secondItem = list.addItem();
    typeText(secondItem, TEST_STRING_ITEM2);

    secondItem.up();

    assertProperSequence(list, TEST_STRING_ITEM2, TEST_STRING_ITEM1);
  }

  @Test
  public void testMoveDown() {
    AemListItem firstItem = list.addItem();
    typeText(firstItem, TEST_STRING_ITEM1);
    typeText(list.addItem(), (TEST_STRING_ITEM2));

    firstItem.down();

    assertProperSequence(list, TEST_STRING_ITEM2, TEST_STRING_ITEM1);
  }

  @Test
  public void testRemove() {
    AemListItem firstItem = list.addItem();
    typeText(firstItem, TEST_STRING_ITEM1);
    typeText(list.addItem(), TEST_STRING_ITEM2);

    firstItem.remove();

    assertProperSequence(list, TEST_STRING_ITEM2);
  }

}
