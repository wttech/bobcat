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
package com.cognifide.qa.bb.provider.selenium.webdriver.creators;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates instances of {@link EdgeDriver}
 */
public class EdgeDriverCreator implements WebDriverCreator {
  private static final Logger LOG = LoggerFactory.getLogger(EdgeDriverCreator.class);
  private static final String ID = "edge";

  @Override
  public WebDriver create(Capabilities capabilities) {
    LOG.info("Starting the initialization of '{}' WebDriver instance", ID);
    LOG.debug("Initializing WebDriver with following capabilities: {}", capabilities);
    return new EdgeDriver(new EdgeOptions().merge(capabilities));
  }

  @Override
  public String getId() {
    return ID;
  }
}
