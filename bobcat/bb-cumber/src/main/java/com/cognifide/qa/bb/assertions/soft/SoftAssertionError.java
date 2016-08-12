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
package com.cognifide.qa.bb.assertions.soft;

/**
 * Thrown to indicate that a soft assertion has failed.
 */
public class SoftAssertionError extends AssertionError {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an AssertionError with its detail message derived from the specified object, which
   * is converted to a string as defined in section 15.18.1.1 of The Java™ Language Specification.
   * If the specified object is an instance of Throwable, it becomes the cause of the newly
   * constructed assertion error.
   * <p>
   * Parameters:
   *
   * @param msg value to be used in constructing detail message
   */
  public SoftAssertionError(String msg) {
    super(msg);
  }

}
