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
package com.cognifide.qa.bb.proxy.analyzer;

import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

/**
 * This filter tests all requests against the given predicate. If some request matches, accepted is set
 * to true and the object is notified.
 */
class PredicateRequestFilter implements RequestFilter {

  private final RequestPredicate predicate;

  private boolean accepted;

  /**
   * Constructor. Initializes PredicateHttpRequestFilter.
   *
   * @param predicate request predicate
   */
  public PredicateRequestFilter(RequestPredicate predicate) {
    this.predicate = predicate;
  }

  /**
   * Return true if the predicate accepted at least one request.
   */
  boolean isAccepted() {
    return accepted;
  }

  @Override
  public HttpResponse filterRequest(HttpRequest request,
      HttpMessageContents contents, HttpMessageInfo messageInfo) {
    if (predicate.accepts(request)) {
      synchronized (this) {
        accepted = true;
        notifyAll();
      }
    }
    return null;
  }
}
