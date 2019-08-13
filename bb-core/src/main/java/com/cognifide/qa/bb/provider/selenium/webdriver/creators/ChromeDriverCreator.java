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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Creates instances of {@link ChromeDriver}
 */
public class ChromeDriverCreator implements WebDriverCreator {

  private static final Logger LOG = LoggerFactory.getLogger(ChromeDriverCreator.class);
  private static final String ID = "chrome";

  @Inject
  @Named(ConfigKeys.CHROME_HEADLESS)
  private boolean headless;

  @Inject
  @Named(ConfigKeys.CHROME_ACCEPT_INSECURE_CERTS)
  private boolean acceptInsecureCerts;

  @Override
  public WebDriver create(Capabilities capabilities) {
    LOG.info("Starting the initialization of '{}' WebDriver instance", ID);
    LOG.debug("Initializing WebDriver with following capabilities: {}", capabilities);
    return new ChromeDriver(getOptions().merge(capabilities));
  }

  @Override
  public String getId() {
    return ID;
  }

  private ChromeOptions getOptions() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.setHeadless(headless);
    chromeOptions.setAcceptInsecureCerts(acceptInsecureCerts);
    return chromeOptions;
  }
}
