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
package com.cognifide.bdd.demo.publish;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.publish.pages.HomePage;
import com.cognifide.bdd.demo.po.publish.pages.MensPage;
import com.cognifide.bdd.demo.po.publish.pages.WomensPage;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class TopNavigationTest {

  @Inject
  private HomePage homePage;

  @Inject
  private MensPage menPage;

  @Inject
  private WomensPage womensPage;

  @Test
  public void menLinkTest() {
    homePage.open();
    assertTrue(homePage.isDisplayed());
    homePage.getTopNav().clickMenLink();
    assertTrue(menPage.isDisplayed());
  }

  @Test
  public void womensLinkTest() {
    homePage.open();
    homePage.getTopNav().clickWomenLink();
    assertTrue(womensPage.isDisplayed());
  }
}
