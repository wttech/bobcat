/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.api;

import static com.cognifide.qa.bb.api.syntax.Assertions.in;
import static com.cognifide.qa.bb.api.syntax.Assertions.seeThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.api.pageobjects.PageHero;
import com.cognifide.qa.bb.api.actions.basic.Navigate;
import com.cognifide.qa.bb.api.actors.Bobcat;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.states.assertions.Visibility;
import com.cognifide.qa.bb.api.states.basic.StateInObject;
import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(TestModule.class)
public class ScreenplayApiTest {

  @Inject
  private Bobcat author;

  @Test
  public void apiSyntaxCompiles() {

    author.attemptsTo(Navigate.to("https://cognifide.github.io/bobcat/"));

    // automagical checks via method references - variations
    author
        .should(seeThat(StateInObject.of(PageHero.class, PageHero::buttonText), is("Get started")));

    author
        .should(seeThat(
            WebElementState.of(PageHero.class, PageHero::button, WebElement::getText),
            is("Get started")));

    author.should(seeThat(in(PageHero.class, PageHero::buttonText), is("Get started")));

    author.should(seeThat(Visibility.of(PageHero.class, PageHero::button), is(true)));

    // direct WebElement check
    PageHero object =
        author.thatCan(PerformBasicOperations.class).instantiate(PageHero.class);
    author.should(seeThat(Visibility.of(object.button()), is(true)));
  }
}
