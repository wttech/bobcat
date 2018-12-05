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
package com.cognifide.qa.bb.javascriptexecutor;

/**
 * Contains common JavaScript that might be used with {@link org.openqa.selenium.JavascriptExecutor}
 */
public class JsScripts {

  private JsScripts() {
    //empty
  }

  /**
   * Selects everything from the active contexts, useful e.g. for selecting the whole text where keyboard shortcuts may be problematic, e.g. on macOS
   */
  public static final String SELECT_ALL = "document.execCommand('selectall',null, false)";
}
