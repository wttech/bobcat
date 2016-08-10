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

import com.cognifide.qa.bb.proxy.analyzer.predicate.ClosestHarEntryElector;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;

import net.lightbody.bmp.core.har.Har;

/**
 * Classes implementing this interface and registered in the Guice are informed about events related to
 * traffic analysis. Please notice that traffic analysis done by the TrafficAnalyzer class takes place in a
 * new thread. Therefore, all ThreadLocal variables (eg. current test in the reporter module) may not work
 * correctly. When the analysis is done, the dispatch() method is run in the thread starting the analysis.
 */
public interface ProxyEventListener {
  /**
   * Method will be invoked when analysis is started.
   */
  void listeningStarted();

  /**
   * Method will be invoked when analysis is finished.
   *
   * @param har Logged requests
   */
  void listeningStopped(Har har);

  /**
   * Method will be invoked after listeningStarted(), when the analyzer is waiting for desired request.
   *
   * @param predicate              RequestPredicate instance.
   * @param closestHarEntryElector object that can find closest object regarding predicate
   *                               might be null value if predicate doesn't allow so
   */
  void waitingForRequest(RequestPredicate predicate, ClosestHarEntryElector closestHarEntryElector);

  /**
   * This method will be invoked if the analyzer finds request.
   */
  void requestFound();

  /**
   * This method will be invoked if the analyzer doesn't find the request.
   */
  void timeout();

  /**
   * Method called after listeningStopped by the original thread starting analysis.
   */
  void dispatch();
}
