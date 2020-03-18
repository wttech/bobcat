package com.cognifide.qa.bb.analytics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoogleAnalyticsTest {

  private static final String EMPTY_FILE_PATH = "";
  private static final String NON_EXISTENT_FILE_PATH = "i-dont-exist";
  private static final String INVALID_FILE_PATH_URI = ">>  << ## %";
  private static final String VALID_FILE_PATH = "ga_pageload";
  private static final String EXPECTED_DATALAYER_STRING =
      "[\r\n"
          + "  {\r\n"
          + "    \"pageName\": \"Home\",\r\n"
          + "    \"pageURL\": \"https://www.cognifide.com/\"\r\n"
          + "  }\r\n"
          + "]\r\n";

  @Spy
  private GoogleAnalytics testedObject;

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
    assertEquals(EXPECTED_DATALAYER_STRING, testedObject.getExpected(VALID_FILE_PATH));
  }

}
