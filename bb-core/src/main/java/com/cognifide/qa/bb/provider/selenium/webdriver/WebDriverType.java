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
package com.cognifide.qa.bb.provider.selenium.webdriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.cognifide.qa.bb.constants.ConfigKeys;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobilePlatform;

/**
 * Enum represent available web driver types.
 */
public enum WebDriverType {
  FIREFOX {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return new FirefoxDriver(new FirefoxOptions(capabilities));
    }
  },
  CHROME {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return new ChromeDriver(new ChromeOptions().merge(capabilities));
    }
  },
  IE {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return new InternetExplorerDriver(new InternetExplorerOptions(capabilities));
    }
  },
  SAFARI {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return new SafariDriver(new SafariOptions(capabilities));
    }
  },
  /**
   * @deprecated Please use an actual browser implementation
   */
  @Deprecated
  HTML {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return new HtmlUnitDriver(capabilities);
    }
  },
  /**
   * @deprecated PhantomJS is no longer maintained, please use headless mode in actual browsers
   */
  @Deprecated
  GHOST {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return new PhantomJSDriver();
    }
  },
  APPIUM {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      return createMobileDriver(capabilities, properties);
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
  },
  REMOTE {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      final URL url;
      try {
        url = new URL(properties.getProperty(ConfigKeys.WEBDRIVER_URL));
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(e);
      }
      return new RemoteWebDriver(url, capabilities);
    }
  },
  XVFB {
    @Override
    public WebDriver create(Capabilities capabilities, Properties properties) {
      final File firefoxPath = new File(properties.getProperty(ConfigKeys.WEBDRIVER_FIREFOX_BIN));
      FirefoxBinary firefoxBinary = new FirefoxBinary(firefoxPath);
      firefoxBinary.setEnvironmentProperty("DISPLAY",
          properties.getProperty(ConfigKeys.WEBDRIVER_XVFB_ID));
      FirefoxOptions firefoxOptions = new FirefoxOptions(capabilities).setBinary(firefoxBinary);

      return new FirefoxDriver(firefoxOptions);
    }
  };

  /**
   * Returns WebDriverType for name
   *
   * @param typeName name of web driver type
   * @return WebDriverType
   */
  public static WebDriverType get(String typeName) {
    return WebDriverType.valueOf(typeName.toUpperCase());
  }

  public abstract WebDriver create(Capabilities capabilities, Properties properties);
}
