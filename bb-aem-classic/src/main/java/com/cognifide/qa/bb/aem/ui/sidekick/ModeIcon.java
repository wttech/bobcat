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
 * This enum represents all view modes available from Sidekick.
 */
public enum ModeIcon {
  EDIT("edit"), PREVIEW("preview"), DESIGN("design"), SCAFFOLDING("scaffolding"), CLIENT_CONTEXT(
      "clientcontext"), WEBSITES("siteadmin"), REOLAD("reload");

  private final String iconName;

  ModeIcon(final String iconName) {
    this.iconName = iconName;
  }

  /**
   * @return Name of the mode.
   */
  public String getIconName() {
    return iconName;
  }
}
