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
package com.cognifide.qa.bb.logging;

/**
 * The ReportEntryLogger interface provides all basic methods
 * for manually creating report log entries.
 */
public interface ReportEntryLogger {
  /**
   * Add an information message to the log.
   *
   * @param message info description
   */
  void info(String message);

  /**
   * Format a message using String.format and passed parameters, then add it to the log.
   *
   * @param message    info description pattern (see {@link String#format(String, Object...)})
   * @param parameters parameters
   */
  void info(String message, Object... parameters);

  /**
   * Add error message to the log.
   *
   * @param message error description
   */
  void error(String message);

  /**
   * Format an error message using String.format and passed parameters, then add it to the log.
   *
   * @param message    error description pattern (see {@link String#format(String, Object...)})
   * @param parameters parameters
   */
  void error(String message, Object... parameters);

  /**
   * Add error message and stacktrace.
   *
   * @param message   error description
   * @param exception original exception
   */
  void error(String message, Throwable exception);

  /**
   * Take and attach a screenshot to the report.
   */
  void screenshot();

  /**
   * Take and attach a screenshot with additional message to the report.
   *
   * @param message screenshot entry message
   */
  void screenshot(String message);

  /**
   * Method to manually add an event entry.
   * In most cases this method is called only by webDriverListener.
   *
   * @param action   name of an event
   * @param param    event parameter
   * @param duration how long the event took (in milliseconds)
   */
  void event(String action, String param, long duration);

  /**
   * Start a subreport section.
   *
   * @param subreport name of subreport section.
   */
  void startSubreport(String subreport);

  /**
   * End a subreport section.
   *
   * @param subreport name of subreport section.
   */
  void endSubreport(String subreport);
}
