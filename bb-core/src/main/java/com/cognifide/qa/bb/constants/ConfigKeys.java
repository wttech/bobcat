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

  public static final String LOGIN_TOKEN = "login.token.name";

  public static final String WEBDRIVER_APPIUM_URL = "webdriver.appium.url";

  public static final String WEBDRIVER_CAP_PLATFORM_NAME = "webdriver.cap.platformName";

  /**
   * @deprecated since 1.6.0; use {@code timings.*} properties
   */
  @Deprecated
  public static final String WEBDRIVER_DEFAULT_TIMEOUT = "webdriver.defaultTimeout";

  public static final String WEBDRIVER_FIREFOX_BIN = "webdriver.firefox.bin";

  public static final String WEBDRIVER_MAXIMIZE = "webdriver.maximize";

  public static final String WEBDRIVER_MOBILE = "webdriver.mobile";

  public static final String WEBDRIVER_REUSABLE = "webdriver.reusable";

  public static final String WEBDRIVER_TYPE = "webdriver.type";

  public static final String WEBDRIVER_XVFB_ID = "webdriver.xvfb.id";

  public static final String WEBDRIVER_URL = "webdriver.url";

  @Deprecated
  public static final String CONFIGURATION_PATHS = "configuration.paths";

  @Deprecated
  public static final String DEFAULT_PROPERTIES_NAME = "default.properties";

  @Deprecated
  public static final String JUNIT_RERUNS = "junit.reruns";

  public static final String CONFIG_STRATEGY = "bobcat.config";
  public static final String WEBDRIVER_PROP_PREFIX = "webdriver.";

  public static final String COOKIES_LOAD_AUTOMATICALLY = "cookies.loadAutomatically";
  public static final String COOKIES_FILE = "cookies.file";

  public static final String MODIFIERS_IMPLICIT_TIMEOUT = "modifiers.implicitTimeout";

  public static final String TIMINGS_EXPLICIT_TIMEOUT = "timings.explicitTimeout";
  public static final String TIMINGS_IMPLICIT_TIMEOUT = "timings.implicitTimeout";
  public static final String TIMINGS_POLLING_INTERVAL = "timings.pollingInterval";

  private ConfigKeys() {
  }
}
