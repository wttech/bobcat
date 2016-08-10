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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.cognifide.bdd.demo.po.geomain.CarouselComponent;
import com.cognifide.bdd.demo.po.geomain.GeometrixxMainPage;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemList;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemListItem;
import com.cognifide.qa.bb.aem.dialog.classic.field.lookup.AemLookupField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.google.inject.Inject;

public class AemListTestHelper {

  public static final String FIXED_LIST_TAB = "Fixed list";

  public static final String TEST_STRING_ITEM3 = "item3";

  public static final String TEST_STRING_ITEM2 = "item2";

  public static final String TEST_STRING_ITEM1 = "item1";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private GeometrixxMainPage geoMatrixMainPage;

  public static void assertProperSequence(AemList list, String... text) {
    if (text.length != list.size()) {
      fail();
    }

    int index = 0;
    for (AemListItem aemListItem : list) {
      assertThat(getText(aemListItem), is(text[index++]));
    }
  }

  public static String getText(AemListItem aemListItem) {
    return aemListItem.getField(AemLookupField.class).read();
  }

  public static void typeText(AemListItem item, String text) {
    item.getField(AemLookupField.class).type(text);
  }

  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
  }

  public AemList openDialogAndGetList() {
    CarouselComponent carouselComponent = geoMatrixMainPage.getCarouselComponent();
    AemDialog dialog = carouselComponent.getDialog();
    dialog.openByContextMenu();
    dialog.clickTab(FIXED_LIST_TAB);

    AemList list = carouselComponent.getList();
    list.clear();
    return list;
  }

  private void openPageToTest() {
    geoMatrixMainPage.open();
    assertTrue(geoMatrixMainPage.isDisplayed());
  }
}
