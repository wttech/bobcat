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
package com.cognifide.qa.bb.aem.content;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;

/**
 * This is a utility class that lets the user send http request to crx.
 */
public class CrxRequestSender {

  @Inject
  private CloseableHttpClient httpClient;

  /**
   * This method sends request to author instance
   *
   * @param request request to send
   * @return JsonObject representation of response
   * @throws IOException if response doesn't contain desired message
   */
  public JsonObject sendCrxRequest(HttpUriRequest request) throws IOException {
    String resultJson;
    try (CloseableHttpResponse response = httpClient.execute(request)) {
      resultJson = EntityUtils.toString(response.getEntity());
    }
    JsonObject result;
    try {
      result = new JsonParser().parse(resultJson).getAsJsonObject();
    } catch (JsonSyntaxException e) {
      throw new JsonSyntaxException("Unable to parse as Json: " + resultJson, e);
    }
    if (result.get("success").getAsBoolean()) {
      return result;
    }
    throw new IOException(result.get("msg").getAsString());
  }

  /**
   * This method sends request to author instance
   *
   * @param request        request to send
   * @param desiredMessage expected message
   * @throws IOException if response doesn't contain desired message
   */
  public void sendCrxRequest(HttpUriRequest request, String desiredMessage) throws IOException {
    String result;
    try (CloseableHttpResponse response = httpClient.execute(request)) {
      result = EntityUtils.toString(response.getEntity());
    }
    if (!result.contains(desiredMessage)) {
      throw new IOException(
          "crx request failure: " + result + " doesn't contain desired message: " + desiredMessage);
    }
  }
}
