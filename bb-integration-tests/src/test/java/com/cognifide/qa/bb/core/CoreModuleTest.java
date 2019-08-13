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
package com.cognifide.qa.bb.core;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.cognifide.qa.bb.modules.CoreModule;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.ChromeDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.EdgeDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.FirefoxDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.IeDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.RemoteDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.SafariDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.WebDriverCreator;
import com.google.inject.Guice;
import com.google.inject.Stage;

class CoreModuleTest {

  @Test
  void allBindingsAreConfiguredCorrectly() {
    Guice.createInjector(Stage.TOOL, new CoreModule());
  }

  @Test
  void allAppropriateCreatorsAreLoadedInTheModule() {
    List<String> expected = Stream.of(
        FirefoxDriverCreator.class,
        IeDriverCreator.class,
        ChromeDriverCreator.class,
        EdgeDriverCreator.class,
        SafariDriverCreator.class,
        RemoteDriverCreator.class)
        .map(Class::getName)
        .collect(toList());

    List<String> actual =
        Guice.createInjector(Stage.TOOL, new CoreModule()).getBindings().entrySet()
            .stream()
            .filter(
                entry -> entry.getKey().getTypeLiteral().getRawType()
                    .equals(WebDriverCreator.class))
            .map(Map.Entry::getValue)
            .map(Objects::toString)
            .map(s -> {
              Matcher matcher =
                  Pattern.compile(".*target=.*type=(.*), annotation=\\[none].*").matcher(s);
              matcher.find();
              return matcher.group(1);
            })
            .collect(toList());

    assertThat(actual).containsAll(expected);
  }

}
