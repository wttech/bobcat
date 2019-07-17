/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.rte;

/**
 * Contains constants related to Rich Text toolbar options
 */
public class Options {
  private Options() {
    //util
  }

  //simple options
  public static final String RTE_OPTIONS = "RTE_OPTIONS";
  public static final String BOLD = "Bold";
  public static final String ITALIC = "Italic";
  public static final String UNDERLINE = "Underline";
  public static final String UNLINK = "Unlink";

  //lists options
  public static final String RTE_OPTIONS_LISTS = "RTE_OPTIONS_LISTS";
  public static final String BULLET_LIST = "Bullet List";
  public static final String NUMBERED_LIST = "Numbered List";
  public static final String OUTDENT = "Outdent";
  public static final String INDENT = "Indent";

  //hyperlink options
  public static final String RTE_OPTIONS_HYPERLINK = "RTE_OPTIONS_HYPERLINK";
  //paragraph options
  public static final String RTE_OPTIONS_PARAGRAPH_FORMATS = "RTE_OPTIONS_PARAGRAPH_FORMATS";

  static final String TOOLBAR_ITEM_CSS =
      "coral-buttongroup.rte-toolbar.is-active .rte-toolbar-item";
}
