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
package com.cognifide.qa.bb.junit.concurrent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.reporter.Reporter;
import com.google.common.base.Joiner;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * ReportingHandler receives asynchronous reporting requests from test threads and redirects them
 * to all Reporter instances.
 * <p>
 * ConcurrentSuite runs this class in a separate thread.
 */
@ThreadScoped
public class ReportingHandler implements Runnable {

  public static final TypeLiteral<Set<Reporter>> ACTIVE_REPORTERS =
      new TypeLiteral<Set<Reporter>>() {};

  private static final String THREAD_NAME = "Reporting thread";

  private static final Logger LOG = LoggerFactory.getLogger(ReportingHandler.class);

  private static final int DEFAULT_SLEEP_TIME = 10000;

  private static final String LOGGED_REPORTER_PREFIX = "\n\t";

  private final Injector injector;

  private volatile boolean keepRunning = true;

  private volatile boolean requestScheduled;

  private volatile Thread asynchronousThread;

  /**
   * Constructs ReportingHandler. Don't call it manually, use Guice.
   *
   * @param injector - injector
   */
  @Inject
  public ReportingHandler(Injector injector) {
    this.injector = injector;
  }

  /**
   * Sets the flag to indicate that there will be no more tests.
   * Waits for reporting thread to finish its job.
   *
   * @throws InterruptedException in case of reporting thread is not running yet.
   */
  public void endOfSuite() throws InterruptedException {
    if (asynchronousThread == null) {
      throw new IllegalStateException("Reporting thread not yet started");
    }
    this.keepRunning = false;
    asynchronousThread.join();
  }

  /**
   * Main method of the handler thread, listening for reporting requests.
   */
  @Override
  public void run() {
    asynchronousThread = Thread.currentThread();

    setThreadName();

    while (true) {

      if (requestScheduled) {
        requestScheduled = false;
        report();
      }

      if (!keepRunning && !requestScheduled) {
        break;
      }

      try {
        TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_TIME);
      } catch (InterruptedException e) {
        LOG.error("Sleep was interrupted", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Schedules reporting request.
   */
  public void generateReport() {
    if (asynchronousThread != null) {
      requestScheduled = true;
    } else {
      report();
    }
  }

  private void setThreadName() {
    asynchronousThread.setName(asynchronousThread.getName() + THREAD_NAME);
  }

  private void report() {
    injector.findBindingsByType(ACTIVE_REPORTERS).stream()
        .map(Binding::getProvider)
        .flatMap(provider -> provider.get().stream())
        .forEach(Reporter::generateReport);

    logReportProviders(injector);
  }

  private void logReportProviders(Injector injector) {
    if (LOG.isInfoEnabled()) {
      List<Binding<Set<Reporter>>> bindings = injector.findBindingsByType(ACTIVE_REPORTERS);
      LOG.info("bound reporters sets: {}", bindings.size());
    }
    if (LOG.isDebugEnabled()) {
      List<Binding<Set<Reporter>>> bindings = injector.findBindingsByType(ACTIVE_REPORTERS);
      for (Binding<Set<Reporter>> binding : bindings) {
        Provider<Set<Reporter>> provider = binding.getProvider();
        Set<Reporter> reporters = provider.get();
        String reportersString = Joiner.on(LOGGED_REPORTER_PREFIX).join(reporters);
        String sb = LOGGED_REPORTER_PREFIX + reportersString;
        LOG.debug("reporters for provider: '{}':{}", provider, sb);
      }
    }
  }
}
