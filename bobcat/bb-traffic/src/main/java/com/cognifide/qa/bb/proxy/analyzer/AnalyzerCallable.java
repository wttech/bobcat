/*-
 * #%L
 * Bobcat Parent
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

import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.proxy.ProxyController;
import com.cognifide.qa.bb.proxy.ProxyEventListener;
import com.cognifide.qa.bb.proxy.RequestFilterRegistry;
import com.cognifide.qa.bb.proxy.analyzer.predicate.ClosestHarEntryElector;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicateImpl;
import com.google.inject.Inject;
import com.google.inject.name.Named;

class AnalyzerCallable implements Callable<Boolean> {

  private static final Logger LOG = LoggerFactory.getLogger(AnalyzerCallable.class);

  @Inject
  private ProxyController controller;

  @Inject
  private RequestFilterRegistry registry;

  @Inject
  private Set<ProxyEventListener> proxyListeners;

  @Inject
  @Named(ConfigKeys.PROXY_ENABLED)
  private boolean proxyEnabled;

  private final RequestPredicate requestPredicate;
  private final ClosestHarEntryElector closestHarEntryElector;

  private final int timeoutInSeconds;

  AnalyzerCallable(RequestPredicate requestPredicate, int timeoutInSeconds) {
    this.requestPredicate = requestPredicate;
    this.timeoutInSeconds = timeoutInSeconds;
    if (requestPredicate instanceof RequestPredicateImpl) {
      this.closestHarEntryElector =
          new ClosestHarEntryElectorImpl((RequestPredicateImpl) requestPredicate);
    } else {
      this.closestHarEntryElector = null;
    }
  }

  @Override
  public Boolean call() throws Exception {
    if (!proxyEnabled) {
      LOG.warn("Proxy is disabled - check {} property", ConfigKeys.PROXY_ENABLED);
      return false;
    }
    PredicateRequestFilter filter = new PredicateRequestFilter(requestPredicate);
    registry.add(filter);
    controller.startAnalysis();
    fireWaitingEvent();
      try {
        filter.wait(timeoutInSeconds * 1000L);
      } catch (InterruptedException e) {
        LOG.error("Interrupted waiting for request", e);
        throw e;
      }
    if (filter.isAccepted()) {
      fireFoundEvent();
    } else {
      fireTimeoutEvent();
    }
    controller.stopAnalysis();
    registry.remove(filter);
    return filter.isAccepted();
  }

  private void fireTimeoutEvent() {
    proxyListeners.forEach(com.cognifide.qa.bb.proxy.ProxyEventListener::timeout);
  }

  private void fireFoundEvent() {
    proxyListeners.forEach(com.cognifide.qa.bb.proxy.ProxyEventListener::requestFound);
  }

  private void fireWaitingEvent() {
    for (ProxyEventListener listener : proxyListeners) {
      listener.waitingForRequest(requestPredicate, closestHarEntryElector);
    }
  }
}
