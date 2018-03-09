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

import java.util.List;
import java.util.logging.Level;

import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;

import com.cognifide.qa.bb.logging.entries.BrowserLogEntry;
import com.cognifide.qa.bb.logging.entries.LogEntry;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Class for gathering browser log entries
 */
public class BrowserLogEntryCollector {

  @Inject
  private WebDriver webDriver;

  /**
   * @return List of log entries retrieved from browser
   */
  public List<LogEntry> getBrowserLogEntries() {
    List<LogEntry> toReturn = Lists.newArrayList();
    try {
      List<org.openqa.selenium.logging.LogEntry> browserEntries =
          webDriver.manage().logs().get(LogType.BROWSER)
              .filter(Level.SEVERE);
      browserEntries.stream().
          forEach(browserEntry ->
              toReturn.add(new BrowserLogEntry(browserEntry.toString()))
          );
    } catch(UnsupportedCommandException e){
      toReturn.add(new BrowserLogEntry("Used driver don't support retrieving console logs"));
    }
    return toReturn;
  }

}
