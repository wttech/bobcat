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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.google.inject.Inject;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobilePlatform;

public class AppiumDriverCreator implements WebDriverCreator {

  @Inject
  private Properties properties;

  @Override
  public WebDriver create(Capabilities capabilities) {
    return createMobileDriver(capabilities, properties);
  }

  @Override
  public String getId() {
    return "appium";
  }

  private WebDriver createMobileDriver(Capabilities capabilities, Properties properties) {
    final URL url;
    try {
      url = new URL(properties.getProperty(ConfigKeys.WEBDRIVER_APPIUM_URL));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }

    final String platform = properties.getProperty(ConfigKeys.WEBDRIVER_CAP_PLATFORM_NAME);
    if (platform == null) {
      throw new IllegalStateException(String.format("%s is missing. Set it either to %s or %s",
          ConfigKeys.WEBDRIVER_CAP_PLATFORM_NAME, MobilePlatform.ANDROID, MobilePlatform.IOS));
    }
    switch (platform) {
      case MobilePlatform.ANDROID:
        return new AndroidDriver(url, capabilities);

      case MobilePlatform.IOS:
        return new IOSDriver(url, capabilities);

      default:
        throw new IllegalArgumentException(String.format(
            "%s is not configured correctly. Set it either to %s or %s",
            ConfigKeys.WEBDRIVER_CAP_PLATFORM_NAME, MobilePlatform.ANDROID, MobilePlatform.IOS));
    }
  }

}
