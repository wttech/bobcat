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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cognifide.qa.bb.proxy.ProxyEventListener;

class DispatchingFuture extends FutureWrapper<Boolean> {

  private final Set<ProxyEventListener> proxyListeners;

  private final AtomicBoolean listenersDispatched;

  DispatchingFuture(Future<Boolean> wrapped, Set<ProxyEventListener> proxyListeners) {
    super(wrapped);
    this.proxyListeners = proxyListeners;
    listenersDispatched = new AtomicBoolean();
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
    proxyListeners.stream().
        forEach(ProxyEventListener::dispatch);
  }
}
