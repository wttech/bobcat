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
package com.cognifide.qa.bb.constants;

public final class ConfigKeys {

  public static final String PROXY_ENABLED = "proxy.enabled";

  public static final String PROXY_IP = "proxy.ip";

  /**
   * Starting port for browser mob proxy servers.
   */
  public static final String PROXY_PORT = "proxy.port";

  public static final String BASE_URL = "base.url";

  public static final String LOGIN_TOKEN = "login.token.name";

  public static final String WEBDRIVER_APPIUM_URL = "webdriver.appium.url";

  public static final String WEBDRIVER_CAP_PLATFORM_NAME = "webdriver.cap.platformName";

  public static final String WEBDRIVER_DEFAULT_TIMEOUT = "webdriver.defaultTimeout";

  public static final String WEBDRIVER_FIREFOX_BIN = "webdriver.firefox.bin";

  public static final String WEBDRIVER_MAXIMIZE = "webdriver.maximize";

  public static final String WEBDRIVER_MOBILE = "webdriver.mobile";

  public static final String WEBDRIVER_PROXY_COOKIE = "webdriver.secure.proxy.cookie";

  public static final String WEBDRIVER_PROXY_COOKIE_NAME = "webdriver.secure.proxy.cookie_name";

  public static final String WEBDRIVER_PROXY_COOKIE_VALUE = "webdriver.secure.proxy.cookie_value";

  public static final String WEBDRIVER_PROXY_COOKIE_DOMAIN = "webdriver.secure.proxy.cookie_domain";

  public static final String WEBDRIVER_REUSABLE = "webdriver.reusable";

  public static final String WEBDRIVER_TYPE = "webdriver.type";

  public static final String WEBDRIVER_XVFB_ID = "webdriver.xvfb.id";

  public static final String WEBDRIVER_URL = "webdriver.url";

  public static final String CONFIGURATION_PATHS = "configuration.paths";

  public static final String DEFAULT_PROPERTIES_NAME = "default.properties";

  public static final String JUNIT_RERUNS = "junit.reruns";

  public static final String CONFIG_STRATEGY = "bobcat.config";
  public static final String WEBDRIVER_PROP_PREFIX = "webdriver.";

  private ConfigKeys() {
  }
}
