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
package com.cognifide.qa.bb.module;

import static com.google.common.base.Throwables.throwIfUnchecked;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class PropertiesLoaderModule extends AbstractModule {

  private final String[] propertiesFileNames;

  public PropertiesLoaderModule(String... propertiesFileNames) {
    Preconditions.checkNotNull(propertiesFileNames);
    this.propertiesFileNames = propertiesFileNames;
  }

  @Override
  protected void configure() {
    try {
      Names.bindProperties(binder(), getProperties());
    } catch (IOException e) {
      throwIfUnchecked(e);
      throw new RuntimeException(e);
    }
  }

  private Properties getProperties() throws IOException {
    Properties properties = new Properties();
    for (String propertiesFileName : propertiesFileNames) {
      try (InputStream inputStream = Resources.getResource(propertiesFileName).openStream()) {
        properties.load(inputStream);
      }
    }
    return properties;
  }
}
