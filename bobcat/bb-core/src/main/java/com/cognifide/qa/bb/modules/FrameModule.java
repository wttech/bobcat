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
package com.cognifide.qa.bb.modules;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.matcher.Matchers.not;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;

import com.cognifide.qa.bb.frame.FrameAspect;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;

public class FrameModule extends AbstractModule {
  @Override
  protected void configure() {
    FrameAspect frameAspect = new FrameAspect();
    requestInjection(frameAspect);
    bindInterceptor(annotatedWith(PageObject.class).or(annotatedWith(Frame.class)), any(),
        frameAspect);
    bindInterceptor(not(annotatedWith(PageObject.class)), annotatedWith(Frame.class), frameAspect);
  }

  @Override
  protected void bindInterceptor(Matcher<? super Class<?>> classMatcher,
      Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
    super.bindInterceptor(classMatcher, NoSyntheticMethodMatcher.INSTANCE.and(methodMatcher),
        interceptors);
  }

}
