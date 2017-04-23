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
package com.cognifide.qa.bb.aem.dialog.classic.field;

/**
 * This enum lists all formatting buttons available in AEM's richtext field.
 */
public enum RtButton {
  //@formatter:off
	BOLD("bold"),
	ITALIC("italic"),
	UNDERLINE("underline"),
	JUSTIFY_LEFT("justifyleft"),
	JUSTIFY_CENTER("justifycenter"),
	JUSTIFY_RIGHT("justifyright"),
	MODIFY_LINK("modifylink"),
	UNLINK("unlink"),
	ANCHOR("anchor"),
	BULLET_LIST("unordered"),
	NUMBERED_LIST("ordered"),
	OUTDENT("outdent"),
	INDENT("indent"),
	REMOVE_STYLE("removestyle");
	//@formatter:on

  private final String css;

  RtButton(String css) {
    this.css = css;
  }

  /**
   * @return Css expression identifying the button.
   */
  public String getCss() {
    return css;
  }
}
