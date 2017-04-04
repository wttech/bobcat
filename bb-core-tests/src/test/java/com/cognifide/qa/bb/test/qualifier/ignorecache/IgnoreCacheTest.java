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

package com.cognifide.qa.bb.test.qualifier.ignorecache;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.test.TestModule;
import com.cognifide.qa.test.pageobjects.qualifier.ignorecache.IgnoreCachePage;
import com.cognifide.qa.test.util.PageUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(TestModule.class)
public class IgnoreCacheTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private IgnoreCachePage ignoreCachePage;

  @Before
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  public void shouldRefindElementAfterTypeChange() {

    try {
      ignoreCachePage.typeIntoTextField("someInput");
      webDriver.navigate().refresh();
      ignoreCachePage.typeIntoTextField("someInput");
    } catch (Exception e) {
      assertNull(e);
    }

  }

  @Test(expected = StaleElementReferenceException.class)
  public void shouldNotRefindElementAfterTypeChange() {
    ignoreCachePage.typeIntoCachedTextField("someInput");
    webDriver.navigate().refresh();
    ignoreCachePage.typeIntoCachedTextField("someInput");

  }

}
