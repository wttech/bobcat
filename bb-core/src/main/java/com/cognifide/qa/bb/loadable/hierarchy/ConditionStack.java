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

import com.cognifide.qa.bb.loadable.context.LoadableComponentContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class ConditionStack {

  private final Deque<LoadableComponentContext> stack;

  /**
   *
   * @param stack Stack with {@link LoadableComponentContext}. Hierarchy of conditions should be taken into account when
   * building this stack.
   */
  public ConditionStack(Deque<LoadableComponentContext> stack) {
    this.stack = stack;
  }

  /**
   *
   * @return Stack with {@link LoadableComponentContext}
   */
  public Deque<LoadableComponentContext> getLoadableContextStack() {
    return new ArrayDeque<>(stack);
  }
}
