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
package com.cognifide.qa.bb.aem.touch.data.pages;

import java.util.Map;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.fasterxml.jackson.core.type.TypeReference;
import com.cognifide.qa.bb.aem.touch.util.YamlReader;
import com.google.inject.Provider;

/**
 * Provide pages from yaml configuration file.
 */
@ThreadScoped
public class PagesProvider implements Provider<Pages> {

  private static final String CONFIG_PATH = "pages";

  private Pages pages;

  /**
   * @return pages that were read from yaml configuration file.
   */
  @Override
  @SuppressWarnings("unchecked")
  public Pages get() {
    if (pages == null) {
      pages = new Pages(readConfig());
    }
    return pages;
  }

  private Map readConfig() {
    TypeReference<Map<String, PageDescription>> typeReference = new TypeReference<Map<String,
        PageDescription>>() {
    };
    return YamlReader.read(CONFIG_PATH, typeReference);
  }
}
