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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.inject.Inject;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class RequestFilterRegistryTest extends AbstractProxyTest {

  @Inject
  private RequestFilterRegistry requestFilterRegistry;

  @Inject
  private RequestFilterRegistry requestFilterRegistryAlias;

  @Mock
  private RequestFilter requestFilter;

  @Before
  public void startProxyServer() throws Exception {
    MockitoAnnotations.initMocks(this);
    guiceInject(this);
  }

  @Test
  public void requestFilterRegistryShouldBeSingleton() {
    assertTrue("RequestFilterRegistry should be thread-scoped",
        requestFilterRegistry == requestFilterRegistryAlias);
  }

  @Ignore("TODO - some problems with timing (works in debug mode)")
  @Test
  public void shouldCallFilterByRegistry() throws IOException {
    // given
    BrowserMobProxy browserMobProxy = new BrowserMobProxyServer();
    startProxyServer(browserMobProxy);
    requestFilterRegistry.add(requestFilter);
    browserMobProxy.addRequestFilter(requestFilterRegistry);
    // when
    DesiredCapabilities capabilities = proxyCapabilities(browserMobProxy);
    visitSamplePage(capabilities);
    browserMobProxy.stop();
    // then
    verify(requestFilter, atLeastOnce()).filterRequest(any(HttpRequest.class),
        any(HttpMessageContents.class), any(HttpMessageInfo.class));

  }
}
