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
package com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.collectors;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.WebDriverModifier;

/**
 * Collector that applies WebDriverModifiers to WebDriver.
 */
public class WebDriverModifyingCollector
    implements Collector<WebDriverModifier, WebDriver, WebDriver> {
  
  private final WebDriver webdriver;

  public WebDriverModifyingCollector(WebDriver webdriver) {
    this.webdriver = webdriver;
  }

  @Override
  public Supplier<WebDriver> supplier() {
    return () -> webdriver;
  }

  @Override
  public BiConsumer<WebDriver, WebDriverModifier> accumulator() {
    return (wd, wdMod) -> wd = wdMod.modify(wd);
  }

  @Override
  public BinaryOperator<WebDriver> combiner() {
    return (webDriver, webDriver2) -> {
      throw new UnsupportedOperationException();
    };
  }

  @Override
  public Function<WebDriver, WebDriver> finisher() {
    return webDriver -> webDriver;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Collections.emptySet();
  }
}
