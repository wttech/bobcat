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
package com.cognifide.qa.bb.journeys;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cognifide.qa.bb.journeys.docs.HomePage;
import com.cognifide.qa.bb.page.BobcatPageFactory;
import com.google.inject.Inject;

abstract class HomepageTest {
  @Inject
  BobcatPageFactory bobcatPageFactory;

  @Test
  void navigationContainsCorrectLinks() {
    List<String> expected = Arrays.asList(
        "https://cognifide.github.io/bobcat/docs/getting-started/",
        "https://cognifide.github.io/bobcat/docs/",
        "https://cognifide.github.io/bobcat/contact/",
        "https://github.com/Cognifide/bobcat"
    );

    HomePage page = bobcatPageFactory.create(HomePage.URL, HomePage.class);

    page.open();
    List<String> actual = page
        .getMasthead()
        .getNav()
        .getUrls();

    assertThat(actual).containsAll(expected);
  }
}
