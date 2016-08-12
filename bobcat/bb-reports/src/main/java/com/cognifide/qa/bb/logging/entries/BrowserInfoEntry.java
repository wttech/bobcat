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

import org.openqa.selenium.Capabilities;

/**
 * This entry stores information about the browser.
 */
public class BrowserInfoEntry extends LogEntry {
  private final Capabilities caps;

  /**
   * Constructs the BrowserInfoEntry.
   *
   * @param capabilities browser capabilities
   */
  public BrowserInfoEntry(Capabilities capabilities) {
    super();
    this.caps = capabilities;
  }

  /**
   * @return Capabilities instance which contains all details about the browser.
   */
  public Capabilities getCapabilities() {
    return caps;
  }
}
