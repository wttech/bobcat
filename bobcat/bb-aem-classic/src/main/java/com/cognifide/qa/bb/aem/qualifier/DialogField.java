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
package com.cognifide.qa.bb.aem.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this annotation to PageObject's fields that represent fields of a dialog. Bobcat will initialize these
 * fields automatically.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DialogField {
  /**
   * @return Label that identifies the dialog field.
   */
  String label() default "";

  /**
   * Use this property only when it's not possible to use label or name to identify the dialog field. Be
   * aware that searching for 'x-form-item' ancestor does not takes place in this case.
   * @return dialog css class
   */
  String css() default "";

  /**
   * @return Name of the dialog field.
   */
  String name() default "";

  /**
   * Relative xpath search selector. Be aware that searching for 'x-form-item' ancestor does not takes place
   * in this case. Use this property only when it's not possible to use label or name to identify the dialog
   * field.
   * @return dialog xpath
   */
  String xpath() default "";
}
