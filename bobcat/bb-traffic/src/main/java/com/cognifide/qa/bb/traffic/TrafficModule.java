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
package com.cognifide.qa.bb.traffic;

import com.cognifide.qa.bb.proxy.ProxyEventListener;
import com.cognifide.qa.bb.proxy.record.TrafficLogProvider;
import com.cognifide.qa.bb.traffic.aspects.RecordTraffic;
import com.cognifide.qa.bb.traffic.aspects.RecordTrafficAspect;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;

/**
 * This class contain all bindings required for traffic analysis. You need to install this module in
 * your own Guice module. After installation, following features will be available:
 * <ul>
 * <li>Proxy Event Listener.
 * </ul>
 */
public class TrafficModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<ProxyEventListener> proxyListenerBinder
        = Multibinder.newSetBinder(binder(), ProxyEventListener.class);
    proxyListenerBinder.addBinding().to(TrafficLogProvider.class);

    RecordTrafficAspect recordTrafficAspect = new RecordTrafficAspect();
    requestInjection(recordTrafficAspect);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(RecordTraffic.class),
        recordTrafficAspect);
  }
}
