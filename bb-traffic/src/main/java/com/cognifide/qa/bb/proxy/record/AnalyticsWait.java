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
package com.cognifide.qa.bb.proxy.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.proxy.RequestFilterRegistry;
import com.cognifide.qa.bb.traffic.constants.TrafficConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

/**
 * A class that provides methods to wait for analytics calls to be sent from the browser
 */
@ThreadScoped
public class AnalyticsWait {

  private static final Logger LOG = LoggerFactory.getLogger(AnalyticsWait.class);

  @Inject
  @Named(TrafficConfigKeys.ANALYTICS_URI_PREFIX)
  private String analyticsUriPrefix;

  @Inject
  @Named(TrafficConfigKeys.AJAX_CALL_TIMEOUT_MS)
  private long timeout;

  /**
   * @param filterRegistry register of filters
   */
  @Inject
  public AnalyticsWait(RequestFilterRegistry filterRegistry) {
    filterRegistry.add(new AnalyticsRequestFilter());
  }

  /**
   * Wait for analytics call. Requires active proxy. Wait time is defined by
   * {@code analytics.call.timeout.ms} property (default: {@code 10000}). The call is identified by
   * URI prefix matching {@code analytics.uri.prefix} property (default: {@code /b/ss})
   */
  public synchronized void waitForAnalyticsCall() {
    try {
      long timeoutPoint = System.currentTimeMillis() + timeout;
      while (System.currentTimeMillis() < timeoutPoint) {
        this.wait();
      }
    } catch (InterruptedException e) {
      LOG.error("Sleep was interrupted", e);
      Thread.currentThread().interrupt();
    }
  }

  private class AnalyticsRequestFilter implements RequestFilter {

    @Override
    public io.netty.handler.codec.http.HttpResponse filterRequest(HttpRequest request,
        HttpMessageContents contents, HttpMessageInfo messageInfo) {
      String path = request.getUri();
      if (path.startsWith(analyticsUriPrefix)) {
        synchronized (this) {
          notifyAll();
        }
      }
      return null;
    }
  }
}
