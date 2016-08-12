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
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.runners.model.RunnerScheduler;

final class ConcurrentClassRunnerScheduler implements RunnerScheduler {

  private final ExecutorService executorService;

  private final CompletionService<Void> completionService;

  private final Queue<Future<Void>> tasks = new LinkedList<>();

  ConcurrentClassRunnerScheduler(Class<?> clazz) {
    int numberOfThreads = clazz.isAnnotationPresent(Concurrent.class) ?
        clazz.getAnnotation(Concurrent.class).threads() :
        (int) (Runtime.getRuntime().availableProcessors() * Concurrent.THREADS_NUMBER_FACTOR);
    NamedThreadFactory namedThreadFactory = new NamedThreadFactory(clazz.getSimpleName());

    executorService = Executors.newFixedThreadPool(numberOfThreads, namedThreadFactory);
    completionService = new ExecutorCompletionService<>(executorService);
  }

  @Override
  public void schedule(Runnable childStatement) {
    tasks.offer(completionService.submit(childStatement, null));
  }

  @Override
  public void finished() {
    QueueFinalizer queueFinalizer = new QueueFinalizer();
    queueFinalizer.finalizeQueue(tasks, completionService, executorService);
  }

}
