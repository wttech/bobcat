/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.core.mapper.field;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.modules.TestModule;
import com.cognifide.qa.bb.core.pageobjects.mapper.field.Item;
import com.cognifide.qa.bb.core.pageobjects.mapper.field.Lists;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.junit5.BobcatExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.google.inject.Inject;

@BobcatExtension
@Modules({TestModule.class})
class PageObjectListCacheTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private Lists lists;

  @BeforeEach
  void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  void shouldCachePageObjectListWhenDecoratedWithCached() {
    int initialSize = lists.getPoList().size();
    int initialCachedSize = lists.getCachedPoList().size();
    lists.addElementToPoList();
    assertThat(lists.getPoList().size()).isEqualTo(++initialSize);
    assertThat(lists.getCachedPoList().size()).isEqualTo(initialCachedSize);
  }

  @Test
  void shouldCacheOnlyListNotEachItemWhenDecoratedWithCached() {
    List<Item> poList = lists.getPoList();
    List<Item> cachedPoList = lists.getCachedPoList();
    assertThat(poList.get(0).getText()).isEqualTo("Coffee");
    assertThat(cachedPoList.get(0).getText()).isEqualTo("Coffee");

    lists.modifyPoListElement();
    assertThat(poList.get(0).getText()).isEqualTo("test");
    assertThat(cachedPoList.get(0).getText()).isEqualTo("test");
  }

  @Test
  void cachedAnnotationDoesNotAffectWebElementList() {
    int initialSize = lists.getWebElementList().size();
    int initialCachedSize = lists.getCachedWebElementList().size();
    lists.addElementToWebelementList();
    assertThat(lists.getWebElementList().size()).isEqualTo(++initialSize);
    assertThat(lists.getCachedWebElementList().size()).isEqualTo(++initialCachedSize);
  }
}
