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

import java.util.LinkedHashSet;
import java.util.Set;

import com.cognifide.qa.bb.guice.ThreadScoped;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

/**
 * This class allows to dynamically register and unregister RequestFilter objects in the current proxy.
 */
@ThreadScoped
public class RequestFilterRegistry implements RequestFilter {

  private final Set<RequestFilter> filters = new LinkedHashSet<>();

  /**
   * Register new filter
   *
   * @param filter filter to be added to the Set of filters.
   */
  public void add(RequestFilter filter) {
    filters.add(filter);
  }

  /**
   * Unregister filter (it won't get anymore events)
   *
   * @param filter filter to be removed to the Set of filters.
   */
  public void remove(RequestFilter filter) {
    filters.remove(filter);
  }

  @Override
  public HttpResponse filterRequest(
      HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
    for (RequestFilter requestFilter : filters) {
      HttpResponse response = requestFilter.filterRequest(httpRequest, httpMessageContents, httpMessageInfo);
      if (response != null) {
        return response;
      }
    }
    return null;
  }
}
