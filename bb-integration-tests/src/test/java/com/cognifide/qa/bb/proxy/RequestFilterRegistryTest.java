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
package com.cognifide.qa.bb.proxy;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.modules.ProxyEnabledModule;
import com.cognifide.qa.bb.junit5.BobcatExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.inject.Inject;

@BobcatExtension
@Modules({ProxyEnabledModule.class})
class RequestFilterRegistryTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private RequestFilterRegistry filters;

  private WireMockServer wireMockServer;

  @BeforeEach
  void setup() {
    wireMockServer = new WireMockServer();
    wireMockServer.start();
  }

  @Test
  void bobcatUserCanAddHeaderWhenUsingProxy() {
    //given
    stubFor(get(urlEqualTo("/proxy-test"))
        .withHeader("Bobcat-Header", equalTo("present"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("works correctly")));

    filters.add((request, contents, messageInfo) -> {
      request.headers().add("Bobcat-Header", "present");
      return null;
    });

    //when
    String url = String.format("http://%s:%s/proxy-test", wireMockServer.getOptions().bindAddress(),
        wireMockServer.getOptions().portNumber());
    webDriver.get(url);

    //then
    assertThat(webDriver.getPageSource()).contains("works correctly");
  }

  @AfterEach
  void teardown() {
    if (wireMockServer != null) {
      wireMockServer.stop();
    }
  }
}
