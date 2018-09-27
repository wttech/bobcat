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
package com.cognifide.qa.bb.cookies;

import java.util.List;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.cookies.domain.CookieData;
import com.cognifide.qa.bb.cookies.domain.CookiesData;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.utils.YamlReader;
import com.google.inject.Provider;

/**
 * This is the default provider of a {@link CookieData} list bundled with Bobcat Core. It reads the selected file from under the {@code cookies} resources folder.
 * <p>
 * By default reads the {@code /cookies/cookies.yaml} file. The filename can be provided in {@code cookies.file} System property.
 */
@ThreadScoped
public class DefaultCookiesProvider implements Provider<List<CookieData>> {

  static final String DEFAULT_FILE_NAME = "/cookies.yaml";
  static final String COOKIES_FOLDER = "/cookies/";

  private List<CookieData> cookiesData;

  /**
   * Provides an instance of {@code List<CookieData>} from the cache, or creates a new one.
   */
  @Override
  public List<CookieData> get() {
    if (cookiesData == null) {
      cookiesData = create();
    }
    return cookiesData;
  }

  /**
   * @return the current path to the file containing cookies definition.
   */
  public static String getPath() {
    return COOKIES_FOLDER + System.getProperty(ConfigKeys.COOKIES_FILE, DEFAULT_FILE_NAME);
  }

  private List<CookieData> create() {
    return YamlReader
        .read(getPath(),
            CookiesData.class).getCookies();
  }
}
