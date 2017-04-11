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
package com.cognifide.qa.bb.cumber.guice;

import com.cognifide.qa.bb.assertions.soft.SoftAssertionsAspect;
import com.cognifide.qa.bb.loadable.CucumberLoadableProcessorFilter;
import com.cognifide.qa.bb.loadable.LoadableProcessorFilter;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.google.inject.multibindings.Multibinder;
import cucumber.api.java.en.And;
import cucumber.api.java.en.But;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.google.inject.matcher.Matchers.any;

public class BobcumberModule extends AbstractModule {

  @Override
  protected void configure() {

    bindListener(any(), new CucumberLoadableProcessorFilter());
    // soft assertion aspect
    SoftAssertionsAspect softAssertAspect = new SoftAssertionsAspect();
    requestInjection(softAssertAspect);
    bindInterceptor(any(), Matchers.annotatedWith(When.class), softAssertAspect);
    bindInterceptor(any(), Matchers.annotatedWith(Then.class), softAssertAspect);
    bindInterceptor(any(), Matchers.annotatedWith(And.class), softAssertAspect);
    bindInterceptor(any(), Matchers.annotatedWith(But.class), softAssertAspect);
    bindInterceptor(any(), Matchers.annotatedWith(Given.class), softAssertAspect);
    Multibinder<LoadableProcessorFilter> processorFilterMultiBinder =
        Multibinder.newSetBinder(binder(), LoadableProcessorFilter.class);
    processorFilterMultiBinder.addBinding().to(CucumberLoadableProcessorFilter.class);

  }
}
