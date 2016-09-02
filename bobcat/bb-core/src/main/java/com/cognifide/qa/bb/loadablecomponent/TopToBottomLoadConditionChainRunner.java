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
package com.cognifide.qa.bb.loadablecomponent;

import com.cognifide.qa.bb.mapper.tree.LoadableContext;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TopToBottomLoadConditionChainRunner implements LoadConditionChainRunner {

  @Override
  public boolean chainCheck(LoadableQualifiersStack conditionStack) {
    Stack<LoadableContext> stack = conditionStack.getLoadableContextStack();
    while (!stack.isEmpty()) {
      LoadableContext loadableContext = stack.pop();
      for (Loadable loadable : loadableContext.getLoadables()) {
        try {
          LoadableComponentCondition componentCondition = (LoadableComponentCondition) loadable.
                  getConditionImplementation().newInstance();

          if (componentCondition.check(loadableContext.getElement(), loadableContext.getLoadables()) == false) {
            return false;
          }
        } catch (InstantiationException | IllegalAccessException ex) {
          Logger.getLogger(TopToBottomLoadConditionChainRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return true;
  }

}
