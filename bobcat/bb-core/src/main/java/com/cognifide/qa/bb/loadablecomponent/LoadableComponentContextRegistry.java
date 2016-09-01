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

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class LoadableComponentContextRegistry {

  private final Map<Class, LoadableQualifiersStack> registry = new HashMap<>();

  /**
   *
   * @param callerClass
   * @return
   */
  public LoadableQualifiersStack getLoadableQualifiersStack(Class callerClass) {
    return registry.get(callerClass);
  }

  /**
   *
   * @param clazz
   * @param stack
   */
  public void registerClassWithLoadableQualifiersStack(Class clazz, LoadableQualifiersStack stack) {
    if (!registry.containsKey(clazz)) {
      registry.put(clazz, stack);
    }
  }

  boolean isQualifiersStackAlreadyDefined(Class callerClass) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
