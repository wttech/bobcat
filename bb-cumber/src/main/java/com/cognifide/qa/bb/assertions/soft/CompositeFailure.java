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

import java.util.List;

/**
 * Exception class that serves as a container for one or more failure reasons.
 */
public class CompositeFailure extends Exception {

  private static final long serialVersionUID = 1L;

  private final List<Throwable> errors;

  /**
   * @param errors list of failures
   */
  public CompositeFailure(List<Throwable> errors) {
    this.errors = errors;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("Composite failure (%d errors)%n", errors.size()));

    int i = 1;
    for (Throwable t : errors) {
      sb.append(String.format(" %d: %s: %s%n", i, t.getClass().getSimpleName(), t.getMessage()));
      i++;
    }
    return sb.toString();
  }

}
