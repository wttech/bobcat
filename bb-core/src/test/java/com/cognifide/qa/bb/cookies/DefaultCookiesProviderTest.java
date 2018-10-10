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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.cognifide.qa.bb.constants.ConfigKeys;

public class DefaultCookiesProviderTest {

  @Test
  public void shouldReturnDefaultFileByDefault() {
    String tested = DefaultCookiesProvider.getPath();

    assertThat(tested)
        .isEqualTo(
            DefaultCookiesProvider.COOKIES_FOLDER + DefaultCookiesProvider.DEFAULT_FILE_NAME);
  }

  @Test
  public void shouldReturnPathToCorrectFileWhenSysPropIsSet() {
    String expectedFile = "expected.yaml";
    System.setProperty(ConfigKeys.COOKIES_FILE, expectedFile);

    String tested = DefaultCookiesProvider.getPath();

    assertThat(tested).isEqualTo(DefaultCookiesProvider.COOKIES_FOLDER + expectedFile);
  }

}
