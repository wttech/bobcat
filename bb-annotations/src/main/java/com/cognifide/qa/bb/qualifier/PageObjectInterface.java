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
package com.cognifide.qa.bb.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PageObjectInterface is used for marking intefaces which are bound to PageObject representations
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PageObjectInterface {

  /**
   * Use this property to identify the page object by css selector
   *
   * @return page object css class
   */
  String css() default "";

  /**
   * Use this property to identify the page object by xpath selector
   *
   * @return page object xpath
   */
  String xpath() default "";

  /**
   * This indicates whether classes annotated with this annotation should or should not generate currentScope
   * web element. When it's true it declares the following field:
   *
   * <code>
   *{@literal @}{@link CurrentScope}
   *{@literal @}{@link com.google.inject.Inject}
   * private {@link org.openqa.selenium.WebElement} currentScope;
   * </code>
   *
   * When this is true, but field of name currentScope has been already defined this switch has no effect and
   * the field will not be generated.
   */
  boolean generateCurrentScope() default true;
}
