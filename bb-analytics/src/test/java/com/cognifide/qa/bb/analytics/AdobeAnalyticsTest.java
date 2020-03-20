/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2020 Cognifide Ltd.
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
package com.cognifide.qa.bb.analytics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdobeAnalyticsTest {

  private static final String EMPTY_FILE_PATH = "";
  private static final String NON_EXISTENT_FILE_PATH = "i-dont-exist";
  private static final String INVALID_FILE_PATH_URI = ">>  << ## %";
  private static final String VALID_FILE_PATH = "aa_valid";
  private static final String EXPECTED_DATA_LAYER_STRING =
      "{\r\n"
          + "  \"pageInfo\": {\r\n"
          + "    \"pageName\": \"SPP\",\r\n"
          + "    \"language\": \"en_us\",\r\n"
          + "    \"siteName\": \"spp\"\r\n"
          + "  },\r\n"
          + "  \"userInfo\": {\r\n"
          + "    \"loginStatus\": \"NotSignedIn\"\r\n"
          + "  },\r\n"
          + "  \"eventInfo\": {\r\n"
          + "    \"eventLabel\": \"pageview\",\r\n"
          + "    \"eventType\": \"pageload\"\r\n"
          + "  }\r\n"
          + "}\r\n";
  private static final String UNEXPECTED_DATA_LAYER_STRING =
      "{\r\n"
          + "  \"pageInfo\": {\r\n"
          + "    \"pageName\": \"this\",\r\n"
          + "    \"language\": \"is\",\r\n"
          + "    \"siteName\": \"wrong\"\r\n"
          + "  }\r\n"
          + "}\r\n";

  @Spy
  private AdobeAnalytics testedObject;

  @ParameterizedTest
  @ValueSource(strings = {
      EMPTY_FILE_PATH,
      NON_EXISTENT_FILE_PATH,
      INVALID_FILE_PATH_URI})
  void getExpected_whenFilePathIsInvalid_thenThrowIllegalStateException(String filePath) {
    assertThrows(IllegalStateException.class, () -> testedObject.getExpected(filePath));
  }

  @Test
  void getExpected_whenFilePathIsNull_thenThrowIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> testedObject.getExpected(null));
  }

  @Test
  void getExpected_whenFilePathIsValid_thenReturnExpectedString() {
    assertEquals(EXPECTED_DATA_LAYER_STRING, testedObject.getExpected(VALID_FILE_PATH));
  }

  @Test
  void compare_whenDataLayersAreEqual_thenDoNotThrowExceptions() {
    doReturn(EXPECTED_DATA_LAYER_STRING).when(testedObject).getActual();
    doReturn(EXPECTED_DATA_LAYER_STRING).when(testedObject).getExpected(any());
    assertDoesNotThrow(() -> testedObject.compare(EXPECTED_DATA_LAYER_STRING));
  }

  @Test
  void compare_whenDataLayersAreDifferent_thenThrowAssertionErrorException() {
    doReturn(UNEXPECTED_DATA_LAYER_STRING).when(testedObject).getActual();
    doReturn(EXPECTED_DATA_LAYER_STRING).when(testedObject).getExpected(any());
    assertThrows(AssertionError.class, () -> testedObject.compare(EXPECTED_DATA_LAYER_STRING));
  }

}
