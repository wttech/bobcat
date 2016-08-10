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
package com.cognifide.qa.bb.aem.ui.sidekick;

/**
 * Enumeration that describes sections of sidekick.
 */
public enum SidekickSection {

  NONE(""),
  CREATE_VERSION("Create Version"),
  RESTORE_VERSION("Restore Version"),
  LAUNCHES("Launches"),
  TIMEWARP("Timewarp"),
  WORKFLOW("Workflow"),
  TRANSLATION("Translation");

  private final String sectionName;

  SidekickSection(final String sectionName) {
    this.sectionName = sectionName;
  }

  /**
   * Gets name of section.
   *
   * @return name of the section's tab.
   */
  public String getSectionName() {
    return sectionName;
  }
}
