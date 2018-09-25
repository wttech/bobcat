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

import static com.cognifide.qa.bb.cookies.Cookies.FILE_NAME;

import java.util.List;

import com.cognifide.qa.bb.cookies.domain.CookieData;
import com.cognifide.qa.bb.cookies.domain.CookiesData;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.utils.YamlReader;
import com.google.inject.Provider;

@ThreadScoped
public class DefaultCookiesProvider implements Provider<List<CookieData>> {

  private List<CookieData> cookiesData;

  @Override
  public List<CookieData> get() {
    if (cookiesData == null) {
      cookiesData = create();
    }
    return cookiesData;
  }

  private List<CookieData> create() {
    return YamlReader.read(FILE_NAME, CookiesData.class).getCookies();
  }
}
