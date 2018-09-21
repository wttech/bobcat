/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.toolbar;

/**
 * Enum represents available standard component toolbar options.
 */
public enum CommonToolbarOptions {
  EDIT("Edit"), //
  CONFIGURE("Configure"), //
  COPY("Copy"), //
  CUT("Cut"), //
  DELETE("Delete"), //
  PASTE("Paste"), //
  GROUP("Group");

  private final String title;

  CommonToolbarOptions(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
