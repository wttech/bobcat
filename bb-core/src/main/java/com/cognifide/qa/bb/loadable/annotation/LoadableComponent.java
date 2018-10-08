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
package com.cognifide.qa.bb.loadable.annotation;

import com.cognifide.qa.bb.loadable.condition.LoadableComponentCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.WebElement;

/**
 *
 * This annotation tells Bobcat that before executing any of {@link WebElement} methods, condition provided in
 * the condClass attribute should be evaluated. When the annotation is placed on a page object field, the
 * underlying WebElements are taken into account supporting the entire hierarchy of downstream page objects
 * and web elements.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(LoadableComponents.class)
@Target(ElementType.FIELD)
public @interface LoadableComponent {

  /**
   * Delay between condition checks
   * @return default delay value, 1 second
   */
  int delay() default 1;

  /**
   * Timeout for positive condition evaluation
   * @return default condition timeout value, 10 seconds
   */
  int timeout() default 10;

  /**
   * Condition implementation to be evaluated. Class have to implement {@link LoadableComponentCondition}
   * interface.
   * @return the condition
   */
  Class<? extends LoadableComponentCondition> condClass();
}
