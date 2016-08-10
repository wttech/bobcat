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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.proxy.ProxyEventListener;
import com.cognifide.qa.bb.proxy.analyzer.predicate.ClosestHarEntryElector;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import net.lightbody.bmp.core.har.Har;

/**
 * This is a provider that will produce TrafficLog instances. It is ThreadScoped, so each thread will receive
 * its own instance of TrafficLogProvider.
 */
@ThreadScoped
public class TrafficLogProvider implements ProxyEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(TrafficLogProvider.class);

  @Inject
  @Named(ConfigKeys.PROXY_ENABLED)
  private boolean proxyEnabled;

  private final List<Har> hars = new LinkedList<>();

  @Override
  public void listeningStarted() {
    // empty
  }

  @Override
  public void listeningStopped(Har har) {
    this.hars.add(har);
  }

  @Override
  public void waitingForRequest(RequestPredicate predicate, ClosestHarEntryElector elector) {
    // empty
  }

  @Override
  public void requestFound() {
    // empty
  }

  @Override
  public void timeout() {
    // empty
  }

  @Override
  public void dispatch() {
    // empty
  }

  /**
   * @return the traffic log collected so far
   */
  public TrafficLog get() {
    if (!proxyEnabled) {
      LOG.warn("Proxy is disabled - check {} property", ConfigKeys.PROXY_ENABLED);
    }
    return new TrafficLog(hars);
  }

  /**
   * discard (clear) all the traffic logs collected so far
   */
  public void discard() {
    hars.clear();
  }

}
