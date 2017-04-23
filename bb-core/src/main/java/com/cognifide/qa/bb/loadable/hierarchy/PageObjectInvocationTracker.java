/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.loadable.hierarchy;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.inject.Singleton;

@Singleton
public class PageObjectInvocationTracker {

  private final LinkedList<RuntimeContext> subjectStack = new LinkedList<>();

  private static final Supplier<LinkedList<Object>> SUPPLIER = LinkedList::new;

  public List<Object> getSubjectStack() {
    return subjectStack.stream().map(RuntimeContext::getSubject).collect(Collectors.
      toCollection(SUPPLIER));
  }

  public void add(Class clazz, Object subject) {
    if (subjectStack.stream().anyMatch(el -> el.getEnclosingClass().equals(clazz))) {
      RuntimeContext runtimeContext;
      do {
        runtimeContext = subjectStack.pollLast();
      } while (!runtimeContext.getEnclosingClass().equals(clazz));
    }
    subjectStack.add(new RuntimeContext(clazz, subject));
  }

  public void clearStack() {
    subjectStack.clear();
  }

  static class RuntimeContext {

    private final Class enclosingClass;

    private final Object subject;

    public RuntimeContext(Class enclosingClass, Object subject) {
      this.enclosingClass = enclosingClass;
      this.subject = subject;
    }

    public Class getEnclosingClass() {
      return enclosingClass;
    }

    public Object getSubject() {
      return subject;
    }

  }

}
