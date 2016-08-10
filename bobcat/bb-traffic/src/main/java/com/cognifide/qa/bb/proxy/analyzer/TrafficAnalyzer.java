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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.proxy.ProxyController;
import com.cognifide.qa.bb.proxy.ProxyEventListener;
import com.cognifide.qa.bb.proxy.RequestFilterRegistry;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicateImpl;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Class allows to intercept and analyze traffic, looking for requests matching some conditions.
 */
public class TrafficAnalyzer {

  @Inject
  private Set<ProxyEventListener> proxyListeners;

  @Inject
  private ProxyController controller;

  @Inject
  private RequestFilterRegistry registry;

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
        new AnalyzerCallable(requestPredicate, timeout, proxyEnabled, proxyListeners, controller,
            registry));
    return new DispatchingFuture(future, proxyListeners);
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
}
