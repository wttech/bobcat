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
package com.cognifide.qa.bb.aem.core.pages;

/**
 * Api for adding and removing test pages to AEM instance
 * @param <T> implementation of {@link TestPageData} for page create/remove
 */
public interface AemTestPageControler<T extends TestPageData> {

  /**
   * Create new test page using passed data
   * @param testPageData data for page creation
   * @throws AemPageManipulationException
   */
  void createTestPage(T testPageData) throws AemPageManipulationException;

  /**
   * Delete existing page using passed data to select page to remove
   * @param testPageData information about page to remove
   * @throws AemPageManipulationException
   */
  void deleteTestPage(T testPageData) throws AemPageManipulationException;

}
