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
package com.cognifide.qa.bb.aem.touch.pageobjects.pages;

/**
 * This interface represents a factory of PublishPage instances. Guice will create and inject the
 * factory for you. See the binding in AemTouchUiModule.
 */
public interface PublishPageFactory {
  /**
   * Factory method that produces PublishPage instances.
   *
   * @param path path to page.
   * @return PublishPage instance for given path.
   */
  PublishPage create(String path);
}
