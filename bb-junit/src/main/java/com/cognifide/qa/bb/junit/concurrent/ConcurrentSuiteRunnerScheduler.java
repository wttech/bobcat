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

import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.RunnerScheduler;

import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverRegistry;

/**
 * Internal test thread scheduler.
 */
final class ConcurrentSuiteRunnerScheduler implements RunnerScheduler {

  private static final String THREAD_COUNT_PROPERTY = "thread.count";

  private final ExecutorService executorService;

  private final CompletionService<Void> completionService;

  private final Queue<Future<Void>> tasks = new LinkedList<>();

  private final Properties properties;

  private final ReportingHandler reportingHandler;

  private final WebDriverRegistry webDriverRegistry;

  /**
   * Constructs ConcurrentSuiteRunnerScheduler.
   *
   * @param clazz
   * @param properties
   * @param reportingHandler
   */
  ConcurrentSuiteRunnerScheduler(Class<?> clazz, Properties properties,
      ReportingHandler reportingHandler, WebDriverRegistry webDriverRegistry) {
    this.properties = properties;
    this.reportingHandler = reportingHandler;
    this.webDriverRegistry = webDriverRegistry;
    executorService = Executors.newFixedThreadPool(determineNumberOfThreads(clazz),
        new NamedThreadFactory(clazz.getSimpleName()));
    completionService = new ExecutorCompletionService<>(executorService);
  }

  /**
   * Schedule a child statement to run
   */
  @Override
  public void schedule(Runnable childStatement) {
    Future<Void> submittedStatement = completionService.submit(childStatement, null);
    tasks.add(submittedStatement);
  }

  /**
   * Performs clean up activities:
   * <ul>
   * <li>cancels and removes all tasks from the queue,
   * <li>shuts down the executor service,
   * <li>tells the reporting handler to generate final report.
   * </ul>
   */
  @Override
  public void finished() {
    QueueFinalizer queueFinalizer = new QueueFinalizer();
    queueFinalizer.finalizeQueue(tasks, completionService, executorService);

    try {
      webDriverRegistry.shutdown();
      reportingHandler.endOfSuite();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

  }

  private int determineNumberOfThreads(Class<?> clazz) {
    String property = properties.getProperty(THREAD_COUNT_PROPERTY);
    int numberOfThreads;
    if (StringUtils.isNotEmpty(property)) {
      numberOfThreads = Integer.parseInt(property);
    } else if (clazz.isAnnotationPresent(Concurrent.class)) {
      numberOfThreads = clazz.getAnnotation(Concurrent.class).threads();
    } else {
      numberOfThreads =
          (int) (Runtime.getRuntime().availableProcessors() * Concurrent.THREADS_NUMBER_FACTOR);
    }
    return numberOfThreads;
  }
}
