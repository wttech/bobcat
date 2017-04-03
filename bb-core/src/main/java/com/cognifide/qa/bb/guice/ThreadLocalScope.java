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
package com.cognifide.qa.bb.guice;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * This class represents thread-local scope. It stores all instances that are local to a thread. Bobcat's core
 * module creates a scope binding using this class. It should not be used directly by Bobcat's users.
 */
public class ThreadLocalScope implements Scope {

  private final ThreadLocal<Map<Key<?>, Object>> threadLocal = new ThreadLocal<>();

  @Override
  public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
    return () -> {
      Map<Key<?>, Object> threadScopedObjects = getThreadScoped();

      if (!threadScopedObjects.containsKey(key)) {
        T newInstance = unscoped.get();
        threadScopedObjects.put(key, newInstance);
      }

      return (T) threadScopedObjects.get(key);
    };
  }

  private Map<Key<?>, Object> getThreadScoped() {
    Map<Key<?>, Object> threadScoped = threadLocal.get();
    if (threadScoped == null) {
      threadScoped = new HashMap<>();
      threadLocal.set(threadScoped);
    }
    return threadScoped;
  }

}
