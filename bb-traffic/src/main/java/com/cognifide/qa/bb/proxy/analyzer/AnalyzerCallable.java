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

class AnalyzerCallable implements Callable<Boolean> {

  private static final Logger LOG = LoggerFactory.getLogger(AnalyzerCallable.class);

  private final ProxyController controller;

  private final RequestFilterRegistry registry;

  private final Set<ProxyEventListener> proxyListeners;

  private final boolean proxyEnabled;

  private final RequestPredicate requestPredicate;

  private final ClosestHarEntryElector closestHarEntryElector;

  private final int timeoutInSeconds;

  AnalyzerCallable(RequestPredicate requestPredicate, int timeoutInSeconds, boolean proxyEnabled,
      Set<ProxyEventListener> proxyListeners, ProxyController controller,
      RequestFilterRegistry registry) {
    this.requestPredicate = requestPredicate;
    this.timeoutInSeconds = timeoutInSeconds;
    this.controller = controller;
    this.registry = registry;
    this.proxyListeners = proxyListeners;
    this.proxyEnabled = proxyEnabled;
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
    long timeoutPoint = System.currentTimeMillis() + (timeoutInSeconds * 1000L);
    synchronized (filter) {
      try {
        while (System.currentTimeMillis() < timeoutPoint) {
          filter.wait();
        }
      } catch (InterruptedException e) {
        LOG.error("Interrupted waiting for request", e);
        throw e;
      }
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
    proxyListeners.stream()
        .forEach(listener -> listener.waitingForRequest(requestPredicate, closestHarEntryElector));
  }
}
