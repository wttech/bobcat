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
import com.cognifide.bdd.demo.po.publish.pages.CartPage;
import com.cognifide.bdd.demo.po.publish.pages.CheckoutPage;
import com.cognifide.bdd.demo.po.publish.pages.MensPage;
import com.cognifide.bdd.demo.po.publish.pages.ProductPage;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class OrderTest {

  @Inject
  private MensPage menPage;

  @Inject
  private ProductPage productPage;

  @Inject
  private CartPage cartPage;

  @Inject
  private CheckoutPage checkoutPage;

  @Test
  public void goToProductPage() {
    menPage.open();
    assertTrue(menPage.isDisplayed());
    menPage.clickAshantiNomadLink();
    assertTrue(productPage.isDisplayed());
  }

  @Test
  public void addToCart() {
    productPage.open();
    assertTrue(productPage.isDisplayed());
    productPage.addToCart();
    assertTrue(cartPage.isDisplayed());
  }

  @Test
  public void checkout() {
    productPage.open();
    assertTrue(productPage.isDisplayed());
    productPage.addToCart();
    assertTrue(cartPage.isDisplayed());
    cartPage.checkout();
    assertTrue(checkoutPage.isDisplayed());
  }

}
