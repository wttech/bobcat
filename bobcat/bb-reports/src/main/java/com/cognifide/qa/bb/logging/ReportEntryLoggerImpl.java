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

import com.google.inject.Inject;

/**
 * ReportEntryLoggerImpl is the default implementation of the ReportEntryLogger interface.
 * It uses TestEventCollector to store log information.
 */
public class ReportEntryLoggerImpl implements ReportEntryLogger {

  @Inject
  private TestEventCollectorImpl testEventCollector;

  @Override
  public void info(String message) {
    testEventCollector.info(message);
  }

  @Override
  public void info(String message, Object... parameters) {
    testEventCollector.info(String.format(message, parameters));
  }

  @Override
  public void error(String message) {
    testEventCollector.error(message);
  }

  @Override
  public void error(String message, Throwable exception) {
    testEventCollector.error(message, exception);
  }

  @Override
  public void error(String message, Object... parameters) {
    testEventCollector.error(String.format(message, parameters));
  }

  @Override
  public void screenshot() {
    testEventCollector.screenshot();
  }

  @Override
  public void screenshot(String message) {
    testEventCollector.screenshot(message);
  }

  @Override
  public void event(String action, String param, long duration) {
    testEventCollector.event(action, param, duration);
  }

  @Override
  public void startSubreport(String subreport) {
    testEventCollector.startSubreport(subreport);
  }

  @Override
  public void endSubreport(String subreport) {
    testEventCollector.endSubreport(subreport);
  }
}
