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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.cognifide.qa.bb.junit5.guice.Modules;
import com.cognifide.qa.bb.modules.CoreModule;
import com.google.inject.Inject;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

@ExtendWith(MockitoExtension.class)
@Modules({CoreModule.class})
public class RequestFilterRegistryTest extends AbstractProxyTest {

  @Inject
  private RequestFilterRegistry requestFilterRegistry;

  @Inject
  private RequestFilterRegistry requestFilterRegistryAlias;

  @Mock
  private RequestFilter requestFilter;

  @Test
  public void requestFilterRegistryShouldBeSingleton() {
    assertSame(requestFilterRegistry, requestFilterRegistryAlias,
        "RequestFilterRegistry should be thread-scoped");
  }

  @Disabled("TODO - some problems with timing (works in debug mode)")
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
