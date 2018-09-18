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
package com.cognifide.qa.bb.modules;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.matcher.Matchers.not;
import static com.google.inject.matcher.Matchers.only;
import static com.google.inject.matcher.Matchers.returns;
import static com.google.inject.matcher.Matchers.subclassesOf;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.loadable.hierarchy.PageObjectInterceptor;
import com.cognifide.qa.bb.loadable.hierarchy.WebElementInterceptor;
import com.cognifide.qa.bb.loadable.mapper.TestObjectTypeListener;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;

/**
 * Module that provides bindings for Loadable components. See {@link LoadableComponent} annotation.
 */
public class LoadablesModule extends AbstractModule {

  @Override
  protected void configure() {
    PageObjectInterceptor pageObjectInterceptor = new PageObjectInterceptor();
    requestInjection(pageObjectInterceptor);
    WebElementInterceptor webElementInterceptor = new WebElementInterceptor();
    requestInjection(webElementInterceptor);
    bindInterceptor(subclassesOf(WebElement.class), not(returns(only(ConditionContext.class))), webElementInterceptor);
    bindInterceptor(annotatedWith(PageObject.class), any(), pageObjectInterceptor);

    bindListener(any(), new TestObjectTypeListener());
  }

  @Override
  protected void bindInterceptor(Matcher<? super Class<?>> classMatcher,
      Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
    super.bindInterceptor(classMatcher, NoSyntheticMethodMatcher.INSTANCE.and(methodMatcher),
        interceptors);
  }
}
