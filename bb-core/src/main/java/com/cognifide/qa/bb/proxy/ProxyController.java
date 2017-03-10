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
package com.cognifide.qa.bb.proxy;

import java.net.InetAddress;
import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;

/**
 * This class can be used to start and stop traffic interception.
 */
public class ProxyController {

  private static final Logger LOG = LoggerFactory.getLogger(ProxyController.class);

  private static final EnumSet<CaptureType> CAPTURE_TYPES = EnumSet.of(
      CaptureType.REQUEST_HEADERS,
      CaptureType.REQUEST_CONTENT,
      CaptureType.RESPONSE_HEADERS,
      CaptureType.RESPONSE_CONTENT);

  private BrowserMobProxy browserMobProxy;

  private final Set<ProxyEventListener> eventListeners;

  private final RequestFilterRegistry filterRegistry;

  private final int port;

  ProxyController(
      Set<ProxyEventListener> eventListeners, RequestFilterRegistry filterRegistry, int port) {
    this.browserMobProxy = new BrowserMobProxyServer();
    this.eventListeners = eventListeners;
    this.filterRegistry = filterRegistry;
    this.port = port;
  }

  public BrowserMobProxy startProxyServer(InetAddress proxyAddress) {
    if (!browserMobProxy.isStarted()) {
      try {
        browserMobProxy.start(port, proxyAddress);
        browserMobProxy.addRequestFilter(filterRegistry);
      } catch (Exception e) {
        LOG.error("Can't start proxy", e);
      }
    }
    return browserMobProxy;
  }

  public void stopProxyServer() {
    if (browserMobProxy.isStarted()) {
      try {
        browserMobProxy.stop();
        // proxy cannot be started again thus new instance should be created
        // for more info see https://github.com/lightbody/browsermob-proxy/issues/264
        browserMobProxy = new BrowserMobProxyServer();
      } catch (Exception e) {
        LOG.error("Can't stop proxy", e);
      }
    }
  }

  public void startAnalysis() {
    browserMobProxy.setHarCaptureTypes(CAPTURE_TYPES);
    browserMobProxy.newHar("page");
    for (ProxyEventListener l : eventListeners) {
      l.listeningStarted();
    }
  }

  public void stopAnalysis() {
    browserMobProxy.disableHarCaptureTypes(CAPTURE_TYPES);
    for (ProxyEventListener l : eventListeners) {
      l.listeningStopped(browserMobProxy.getHar());
    }
  }
}
