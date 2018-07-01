/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.logging.allure;

import com.cognifide.qa.bb.logging.allure.reporter.AllureReporter;
import com.cognifide.qa.bb.logging.reporter.provider.CustomReportBinder;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Guice module that will allow Allure Reporter to be run
 */
public class AllureModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<CustomReportBinder> customReportersBinder = Multibinder.newSetBinder(binder(),
        CustomReportBinder.class);
    customReportersBinder.addBinding()
        .toInstance(new CustomReportBinder("allure", AllureReporter.class));
  }
}
