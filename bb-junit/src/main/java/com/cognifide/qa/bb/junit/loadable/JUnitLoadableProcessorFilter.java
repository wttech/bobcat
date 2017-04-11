/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.junit.loadable;

import com.cognifide.qa.bb.loadable.LoadableProcessorFilter;
import com.cognifide.qa.bb.loadable.mapper.TestClassInjectionListener;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class is registering every jUnit runner class in the context of Loadable Components
 */
public class JUnitLoadableProcessorFilter implements LoadableProcessorFilter, TypeListener {

  @Override
  public boolean isApplicable(Class clazz) {
    return isJunitTestRunner(clazz);
  }

  private boolean isJunitTestRunner(Class clazz) {
    return clazz != null && !clazz.isAnnotationPresent(CucumberOptions.class)
        && clazz.isAnnotationPresent(RunWith.class);
  }

  @Override
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    if (isJunitTestRunner(type.getRawType())) {
      encounter.register(new TestClassInjectionListener(encounter));
    }
  }
}
