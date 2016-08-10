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
package com.cognifide.qa.bb.scope;

import java.util.ArrayDeque;
import java.util.Deque;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.scope.frame.FramePath;

/**
 * This thread-scoped class queues PageObjectContext objects to keep track of frames and WebElements as we
 * move down the hierarchy of PageObjects.
 */
@ThreadScoped
public class ContextStack {
  private final Deque<PageObjectContext> deque;

  /**
   * Constructs ContextStack, initializes context stack to an empty queue.
   */
  public ContextStack() {
    deque = new ArrayDeque<>();
  }

  /**
   * @return True if the context stack is empty.
   */
  public boolean isEmpty() {
    return deque.isEmpty();
  }

  /**
   * Takes the top context object off the queue and returns it.
   *
   * @return Top context from the queue.
   */
  public PageObjectContext pop() {
    return deque.pop();
  }

  /**
   * Return top context from the queue, without removing it.
   *
   * @return Top context from the queue.
   */
  public PageObjectContext peek() {
    return deque.peek();
  }

  /**
   * Puts context objects on top of the context stack.
   *
   * @param element The context object to put on top of the stack.
   */
  public void push(PageObjectContext element) {
    deque.push(element);
  }

  /**
   * This is like peek, but returns the default context when the context stack is empty. Default context
   * means top frame and whole page.
   *
   * @param webDriver WebDriver instance that will serve as a base for creating the default context in case
   *                  the context stack is empty.
   * @return Current context, either taken from the context stack or the default context if stack is empty.
   */
  public PageObjectContext getCurrentContext(WebDriver webDriver) {
    return isEmpty() ? getDefaultPageObjectContext(webDriver) : peek();
  }

  private PageObjectContext getDefaultPageObjectContext(WebDriver webDriver) {
    ElementLocatorFactory elementLocatorFactory = new DefaultElementLocatorFactory(webDriver);
    FramePath framePath = new FramePath();
    return new PageObjectContext(elementLocatorFactory, framePath);
  }
}
