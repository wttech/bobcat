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
package com.cognifide.qa.bb.proxy.providers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.proxy.ProxyController;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;

public class DefaultProxyProvider implements Provider<Proxy> {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultProxyProvider.class);

  @Inject
  @Named(ConfigKeys.PROXY_IP)
  private String proxyIp;

  @Inject
  private ProxyController proxyController;

  private Proxy proxy;

  @Override
  public Proxy get() {
    if (proxy == null) {
      proxy = createProxy();
    }
    return proxy;
  }

  private Proxy createProxy() {
    InetAddress proxyInetAddress = null;
    try {
      proxyInetAddress = InetAddress.getByName(proxyIp);
    } catch (UnknownHostException e) {
      LOG.error("Failed to parse by name the provided IP address: {}", proxyIp, e);
    }
    BrowserMobProxy browserMobProxy = proxyController.startProxyServer(proxyInetAddress);
    return ClientUtil.createSeleniumProxy(browserMobProxy, proxyInetAddress);
  }
}
