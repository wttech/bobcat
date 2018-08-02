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

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableMap;

import io.netty.handler.codec.http.HttpRequest;

@RunWith(Parameterized.class)
public class RequestPredicateImplTest {

  private static final String VALID_URL = "/common";

  private static final String VALID_LONG_URL = VALID_URL + "/internal/requestUrl";

  private static final String INVALID_LONG_URL = "http://non-example.com/";

  private static final Map<String, String> EXPECTED_PARAMETERS = ImmutableMap.<String, String>builder()
      .put("some-param", "some-value")
      .build();
  private static final Map<String, String> EMPTY_PARAMETERS = new HashMap<>();

  @Parameters(name = "{index}: {0} | {1} | {2} | {3} = {4} ")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][]{
        //@formatter:off
        {VALID_LONG_URL,    EMPTY_PARAMETERS,     VALID_URL, EMPTY_PARAMETERS,    true},
        {INVALID_LONG_URL,  EMPTY_PARAMETERS,     VALID_URL, EMPTY_PARAMETERS,    false},
        {VALID_LONG_URL,    EMPTY_PARAMETERS,     VALID_URL, EXPECTED_PARAMETERS, false},
        {VALID_LONG_URL,    EXPECTED_PARAMETERS,  VALID_URL, EXPECTED_PARAMETERS, true}
        //@formatter:on
    });
  }

  private final String requestUrl;

  private final Map<String, String> requestParameters;

  private final String predicateUrlPrefix;

  private final Map<String, String> predicateExpectedParameters;

  private final boolean expectedResult;

  public RequestPredicateImplTest(
      String requestUrl, Map<String, String> requestParameters, String predicateUrlPrefix,
      Map<String, String> predicateExpectedParameters, boolean expectedResult) {
    this.requestUrl = requestUrl;
    this.requestParameters = requestParameters;
    this.predicateUrlPrefix = predicateUrlPrefix;
    this.predicateExpectedParameters = predicateExpectedParameters;
    this.expectedResult = expectedResult;
  }

  @Test
  public void shouldReturnRightResponseWhenTestingRequestAgainst() {
    // given
    RequestPredicateImpl tested = new RequestPredicateImpl(predicateUrlPrefix,
        predicateExpectedParameters);
    HttpRequest request = createMockedHttpRequest(requestUrl, createQueryString(requestParameters));

    // when
    boolean acceptationResult = tested.accepts(request);

    // then
    Assert.assertEquals(acceptationResult, this.expectedResult);
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
