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

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Class provides ProxyController instance regarding to thread which is executed with.
 * <br>
 * Internally its using providers for both sets with ProxyEventListeners and RegistryFilters
 * regarding to scope which is thread and both instances cannot be injected by Guice in singleton
 * object once. Thus, both objects are dynamically provided with @ThreadScope annotation.
 */
@Singleton
public class ProxyControllerProvider extends ThreadLocal<ProxyController>
    implements Provider<ProxyController> {

  @Inject
  private Provider<Set<ProxyEventListener>> proxyEventListenersProvider;

  @Inject
  private Provider<RequestFilterRegistry> requestFilterRegistryProvider;

  @Inject
  @Named(ConfigKeys.PROXY_PORT)
  private int startPort;

  private AtomicInteger nextPort;

  @Override
  public synchronized ProxyController initialValue() {
    if (nextPort == null) {
      nextPort = new AtomicInteger(startPort);
    }
    return new ProxyController(proxyEventListenersProvider.get(),
        requestFilterRegistryProvider.get(), nextPort.getAndIncrement());
  }
}
