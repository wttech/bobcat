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
package com.cognifide.qa.bb.proxy.analyzer.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.ImmutableMap;

import io.netty.handler.codec.http.HttpRequest;

public class RequestPredicateImplTest {

  private static final String VALID_URL = "/common";

  private static final String VALID_LONG_URL = VALID_URL + "/internal/requestUrl";

  private static final String INVALID_LONG_URL = "http://non-example.com/";

  private static final Map<String, String> EXPECTED_PARAMETERS =
      ImmutableMap.<String, String>builder()
          .put("some-param", "some-value")
          .build();
  private static final Map<String, String> EMPTY_PARAMETERS = new HashMap<>();

  private static Stream<Arguments> data() {
    return Stream.of(
        arguments(VALID_LONG_URL, EMPTY_PARAMETERS, VALID_URL, EMPTY_PARAMETERS, true),
        arguments(INVALID_LONG_URL, EMPTY_PARAMETERS, VALID_URL, EMPTY_PARAMETERS, false),
        arguments(VALID_LONG_URL, EMPTY_PARAMETERS, VALID_URL, EXPECTED_PARAMETERS, false),
        arguments(VALID_LONG_URL, EXPECTED_PARAMETERS, VALID_URL, EXPECTED_PARAMETERS, true)
    );
  }

  @ParameterizedTest(name = "{index}: {0} | {1} | {2} | {3} = {4} ")
  @MethodSource("data")
  public void shouldReturnRightResponseWhenTestingRequestAgainst(
      String requestUrl, Map<String, String> requestParameters, String predicateUrlPrefix,
      Map<String, String> predicateExpectedParameters, boolean expectedResult) {
    // given
    RequestPredicateImpl tested = new RequestPredicateImpl(predicateUrlPrefix,
        predicateExpectedParameters);
    HttpRequest request = createMockedHttpRequest(requestUrl, createQueryString(requestParameters));

    // when
    boolean acceptationResult = tested.accepts(request);

    // then
    assertEquals(acceptationResult, expectedResult);
  }

  private HttpRequest createMockedHttpRequest(String path, String queryString) {
    HttpRequest request = mock(HttpRequest.class, RETURNS_DEEP_STUBS);
    when(request.getUri()).thenReturn(path + "?" + queryString);
    return request;
  }

  private String createQueryString(Map<String, String> params) {
    if (params.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    params.forEach((key, value) ->
        sb.append(key).append("=").append(value)
    );
    return sb.toString();
  }
}
