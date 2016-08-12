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
package com.cognifide.qa.bb.logging.entries;

import org.apache.commons.lang3.StringUtils;

/**
 * This class represents an exception that was caught during execution of the test.
 */
public class ExceptionEntry extends LogEntry {

  private static final int MAX_MESSAGE_LENGTH = 2000;

  private final String message;

  private final Throwable exception;

  /**
   * Constructs an ExceptionEntry instance.
   *
   * @param exception source exception for log entry
   */
  public ExceptionEntry(Throwable exception) {
    super();
    this.exception = exception;
    this.message = StringUtils.abbreviate(exception.getMessage(), MAX_MESSAGE_LENGTH);
  }

  /**
   * Constructs an ExceptionEntry instance.
   *
   * @param message   description of an exception
   * @param exception original exception
   */
  public ExceptionEntry(String message, Throwable exception) {
    super();
    this.message = message;
    this.exception = exception;
  }

  /**
   * @return User-provided message associated with the exception.
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return Exception that was caught during test
   */
  public Throwable getException() {
    return exception;
  }

  /**
   * @return Stack of the exception
   */
  public StackTraceElement[] getStack() {
    return exception.getStackTrace();
  }
}
