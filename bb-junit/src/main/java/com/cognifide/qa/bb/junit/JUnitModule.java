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
package com.cognifide.qa.bb.junit;

import com.cognifide.qa.bb.junit.loadable.JUnitLoadableProcessorFilter;
import com.cognifide.qa.bb.loadable.LoadableProcessorFilter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import static com.google.inject.matcher.Matchers.any;

/**
 * This class contains binding to the {@link JUnitLoadableProcessorFilter} which allows jUnit tests
 * to be run with Loadable Conditions context
 */
public class JUnitModule extends AbstractModule {

  @Override
  protected void configure() {
    bindListener(any(), new JUnitLoadableProcessorFilter());
    Multibinder<LoadableProcessorFilter> processorFilterMultiBinder =
        Multibinder.newSetBinder(binder(), LoadableProcessorFilter.class);
    processorFilterMultiBinder.addBinding().to(JUnitLoadableProcessorFilter.class);
  }
}
