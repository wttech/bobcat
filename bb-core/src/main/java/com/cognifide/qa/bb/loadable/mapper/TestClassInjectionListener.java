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
package com.cognifide.qa.bb.loadable.mapper;

import com.cognifide.qa.bb.loadable.hierarchy.ConditionsExplorer;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;

class TestClassInjectionListener implements InjectionListener {

  private final Provider<ConditionsExplorer> loadablesExplorer;

  public TestClassInjectionListener(TypeEncounter typeEncounter) {
    this.loadablesExplorer = typeEncounter.getProvider(ConditionsExplorer.class);
  }

  @Override
  public void afterInjection(Object injectee) {
    loadablesExplorer.get().registerLoadableContextHierarchyTree(injectee);
  }

}
