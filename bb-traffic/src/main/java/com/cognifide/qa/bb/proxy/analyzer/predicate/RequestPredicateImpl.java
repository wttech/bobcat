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

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.google.common.base.Charsets;

import io.netty.handler.codec.http.HttpRequest;

/**
 * A simple predicate that matches requests with given URI and parameters.
 */
public class RequestPredicateImpl implements RequestPredicate {

  private final String urlPrefix;

  private final Map<String, String> expectedParams;

  /**
   * Constructor. Initializes RequestPredicateImpl.
   *
   * @param urlPrefix      url prefix
   * @param expectedParams map of expected parameters
   */
  public RequestPredicateImpl(String urlPrefix, Map<String, String> expectedParams) {
    this.urlPrefix = urlPrefix;
    this.expectedParams = expectedParams;
  }

  @Override
  public boolean accepts(HttpRequest request) {
    boolean result = false;
    URI uri = URI.create(request.getUri());
    String path = uri.getPath();
    if (path != null && path.startsWith(urlPrefix)) {
      String query = uri.getQuery();
      if (expectedParams.isEmpty() && StringUtils.isEmpty(query)) {
        result = true;
      } else if (StringUtils.isNotEmpty(query)) {
        List<NameValuePair> params = URLEncodedUtils.parse(query, Charsets.UTF_8);
        result = hasAllExpectedParams(expectedParams, params);
      }
    }
    return result;
  }

  private boolean hasAllExpectedParams(Map<String, String> expected, List<NameValuePair> actual) {
    for (Entry<String, String> expectedParam : expected.entrySet()) {
      if (!findParam(expectedParam.getKey(), expectedParam.getValue(), actual)) {
        return false;
      }
    }
    return true;
  }

  public boolean startsWithUrlPrefix(String url) {
    return url.startsWith(urlPrefix);
  }

  public int calculateScore(Map<String, String> parameters) {
    int result = 0;
    for (Entry<String, String> entry : parameters.entrySet()) {
      if (expectedParams.containsKey(entry.getKey()) && expectedParams.get(entry.getKey())
          .equals(entry.getValue())) {
        result++;
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return String.format("[prefix: %s, params: %s]", urlPrefix, expectedParams);
  }

  private boolean findParam(String key, String value, List<NameValuePair> params) {
    for (NameValuePair param : params) {
      if (param.getName().equals(key) && param.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }
}
