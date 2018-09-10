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
package com.cognifide.qa.bb.aem.core.pages;

/**
 * Exception thrown if there is problem with page manipulation
 */
public class AemPageManipulationException extends Exception {

  public AemPageManipulationException() {
  }

  public AemPageManipulationException(String message) {
    super(message);
  }

  public AemPageManipulationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AemPageManipulationException(Throwable cause) {
    super(cause);
  }

  public AemPageManipulationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
