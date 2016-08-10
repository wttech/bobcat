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
package com.cognifide.qa.bb.aem.ui.component;

/**
 * This exception is thrown when user tries to create a AemComponentHandler from a class that is not annotated
 * with AemComponent annotation.
 */
public class ComponentAnnotationMissingException extends RuntimeException {

  private static final long serialVersionUID = -167384867002189880L;

  /**
   * Constructs ComponentAnnotationMissingException.
   *
   * @param message the detail message.
   */
  public ComponentAnnotationMissingException(String message) {
    super(message);
  }
}
