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
package com.cognifide.qa.bb.logging;

import org.openqa.selenium.support.events.WebDriverEventListener;

import com.cognifide.qa.bb.junit.TestEventCollector;
import com.cognifide.qa.bb.junit.concurrent.ReportingHandler;
import com.cognifide.qa.bb.logging.reporter.provider.ReporterProvider;
import com.cognifide.qa.bb.logging.subreport.Subreport;
import com.cognifide.qa.bb.logging.subreport.SubreportInterceptor;
import com.cognifide.qa.bb.proxy.ProxyEventListener;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;

/**
 * Install this module to enable reporting capabilities. Following features are available:
 * <ul>
 * <li>ReportEntryLogger binding,
 * <li>Reporter instances binding, constructed based on user-defined property,
 * <li>Subreport aspect that looks for Subreport annotations on your test method
 * and automatically creates opening and closing subreport log entries,
 * <li>WebDriverEventListener binding.
 * </ul>
 */
public class ReporterModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ReportEntryLogger.class).to(ReportEntryLoggerImpl.class);
    bind(TestEventCollector.class).to(TestEventCollectorImpl.class);
    bind(ReportingHandler.ACTIVE_REPORTERS).toProvider(ReporterProvider.class).in(Singleton.class);

    SubreportInterceptor subreportInterceptor = new SubreportInterceptor();
    requestInjection(subreportInterceptor);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Subreport.class), subreportInterceptor);

    Multibinder<WebDriverEventListener> webDriverListenerBinder = Multibinder.newSetBinder(binder(),
        WebDriverEventListener.class);
    webDriverListenerBinder.addBinding().to(WebDriverLogger.class);

    Multibinder<ProxyEventListener> proxyListenerBinder = Multibinder.newSetBinder(binder(),
        ProxyEventListener.class);
    proxyListenerBinder.addBinding().to(ProxyLogger.class);

  }
}
