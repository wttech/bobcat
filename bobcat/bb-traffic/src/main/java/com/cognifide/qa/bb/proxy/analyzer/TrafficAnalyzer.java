package com.cognifide.qa.bb.proxy.analyzer;

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


import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

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

/**
 * Class allows to intercept and analyze traffic, looking for requests matching some conditions.
 */
public class TrafficAnalyzer {

  private static final Logger LOG = LoggerFactory.getLogger(TrafficAnalyzer.class);

  @Inject
  private ProxyController controller;

  @Inject
  private RequestFilterRegistry registry;

  @Inject
  private Set<ProxyEventListener> proxyListeners;

  @Inject
  @Named(ConfigKeys.PROXY_ENABLED)
  private boolean proxyEnabled;

  /**
   * Start analysis process, looking for requests matching given predicate.
   *
   * @param requestPredicate Object describing desired requests.
   * @param timeout          How long should we wait for requests matching requestPredicate (in seconds)
   * @return Future object representing the analysis.
   */
  public Future<Boolean> analyzeTraffic(final RequestPredicate requestPredicate,
      final int timeout) {
    Future<Boolean> future = Executors.newSingleThreadExecutor().submit(
        new AnalyzerCallable(requestPredicate, timeout));
    return new DispatchingFuture(future);
  }

  /**
   * Start analysis process, looking for requests matching given predicate.
   *
   * @param uriPrefix      Path prefix we are looking for, eg. /content/zg
   * @param expectedParams Map of expected parameters (GET or POST)
   * @param timeout        How long should we wait for matching requests (in seconds)
   * @return Future object representing the analysis.
   */
  public Future<Boolean> analyzeTraffic(String uriPrefix, Map<String, String> expectedParams,
      final int timeout) {
    return analyzeTraffic(new RequestPredicateImpl(uriPrefix, expectedParams), timeout);
  }

  private final class DispatchingFuture extends FutureWrapper<Boolean> {
    private AtomicBoolean listenersDispatched = new AtomicBoolean();

    private DispatchingFuture(Future<Boolean> wrapped) {
      super(wrapped);
    }

    @Override
    public Boolean get() throws InterruptedException, ExecutionException {
      Boolean result = super.get();
      if (!listenersDispatched.getAndSet(true)) {
        dispatch();
      }
      return result;
    }

    @Override
    public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
        TimeoutException {
      Boolean result = super.get(timeout, unit);
      if (!listenersDispatched.getAndSet(true)) {
        dispatch();
      }
      return result;
    }

    private void dispatch() {
      for (ProxyEventListener l : proxyListeners) {
        l.dispatch();
      }
    }
  }

  private final class AnalyzerCallable implements Callable<Boolean> {
    private final RequestPredicate requestPredicate;
    private final ClosestHarEntryElector closestHarEntryElector;

    private final int timeoutInSeconds;

    private AnalyzerCallable(RequestPredicate requestPredicate, int timeoutInSeconds) {
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
      synchronized (filter) {
        try {
          filter.wait(timeoutInSeconds * 1000L);
        } catch (InterruptedException e) {
          LOG.error("Interrupted waiting for request", e);
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
      for (ProxyEventListener l : proxyListeners) {
        l.timeout();
      }
    }

    private void fireFoundEvent() {
      for (ProxyEventListener l : proxyListeners) {
        l.requestFound();
      }
    }

    private void fireWaitingEvent() {
      for (ProxyEventListener l : proxyListeners) {
        l.waitingForRequest(requestPredicate, closestHarEntryElector);
      }
    }
  }
}
