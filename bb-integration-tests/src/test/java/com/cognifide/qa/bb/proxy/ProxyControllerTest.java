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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.inject.Inject;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class ProxyControllerTest extends AbstractProxyTest {

  @Inject
  private ProxyController proxyController;

  @Inject
  private RequestFilterRegistry requestFilterRegistry;

  @Mock
  private RequestFilter requestFilter;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    guiceInject(this);
  }

  @Ignore("TODO - some problems with timing (works in debug mode)")
  @Test
  public void shouldCallFilterByRegistry() throws IOException {
    // given
    BrowserMobProxy proxy = proxyController.startProxyServer(InetAddress.getLocalHost());
    proxyController.startAnalysis();
    requestFilterRegistry.add(requestFilter);
    // when
    DesiredCapabilities capabilities = proxyCapabilities(proxy);
    visitSamplePage(capabilities);
    proxyController.stopAnalysis();
    proxyController.stopProxyServer();
    // then
    verify(requestFilter, atLeastOnce()).filterRequest(any(HttpRequest.class),
        any(HttpMessageContents.class), any(HttpMessageInfo.class));
  }
}
