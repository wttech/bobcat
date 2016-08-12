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

import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * This class cleans tasks queue.
 */
public class QueueFinalizer {

  /**
   * Removes all task from queue and shuts down executor service.
   *
   * @param taskQueue         Queue of tasks
   * @param completionService Producer of new asynchronous tasks
   * @param executorService   Tasks executor service
   */
  public void finalizeQueue(Queue<Future<Void>> taskQueue,
      CompletionService<Void> completionService, ExecutorService executorService) {
    try {
      while (!taskQueue.isEmpty()) {
        taskQueue.remove(completionService.take());
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      while (!taskQueue.isEmpty()) {
        taskQueue.poll().cancel(true);
      }
      executorService.shutdownNow();
    }
  }
}
