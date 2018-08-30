/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under theComponent Apache License, Version 2.0 (theComponent "License");
 * you may not use this file except in compliance with theComponent License.
 * You may obtain a copy of theComponent License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under theComponent License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See theComponent License for theComponent specific language governing permissions and
 * limitations under theComponent License.
 * #L%
 */
package com.cognifide.qa.bb.proxy;

import java.net.UnknownHostException;
import java.util.Properties;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.modules.CoreModule;
import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverType;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;

abstract class AbstractProxyTest {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractProxyTest.class);

  private static final String EXAMPLE_HTML = "<html><body>Hello</body></html>";

  @Inject
  @Named(ConfigKeys.WEBDRIVER_TYPE)
  private String type;

  @Inject
  private Properties properties;

  void guiceInject(Object instance) {
    Injector injector = Guice.createInjector(new CoreModule());
    injector.injectMembers(instance);
  }

  void startProxyServer(BrowserMobProxy browserMobProxy) throws UnknownHostException {
    int port = browserMobProxy.getPort();
    browserMobProxy.start(port);
    LOG.debug("proxy server started on port: '{}'", port);
  }

  void visitSamplePage(DesiredCapabilities capabilities) {
    try (TextResponseEmbedHttpServer server = new TextResponseEmbedHttpServer(1234, EXAMPLE_HTML)) {
      WebDriverType webDriverType = WebDriverType.get(type);
      WebDriver webDriver = webDriverType.create(capabilities, properties);
      webDriver.get(server.getPath());
      LOG.debug("visited page at: '{}'", server.getPath());
      webDriver.quit();
      LOG.debug("web driver closed and quited");
    }
  }

  DesiredCapabilities proxyCapabilities(BrowserMobProxy browserMobProxy) {
    browserMobProxy.enableHarCaptureTypes(
        CaptureType.REQUEST_HEADERS,
        CaptureType.RESPONSE_HEADERS,
        CaptureType.REQUEST_CONTENT,
        CaptureType.RESPONSE_CONTENT);

    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
    return capabilities;
  }
}
