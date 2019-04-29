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
package com.cognifide.qa.bb.health.service;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.cognifide.qa.bb.health.model.Result;
import com.google.gson.Gson;

public class HealthCheckService {

  private final CloseableHttpClient closeableHttpClient;

  private final Gson gson;

  @Inject
  public HealthCheckService(CloseableHttpClient closeableHttpClient, Gson gson) {
    this.closeableHttpClient = closeableHttpClient;
    this.gson = gson;
  }

  public Result executeHealthCkech() {
    HttpGet httpGet = prepareRequest();
    try (CloseableHttpResponse response = closeableHttpClient.execute(httpGet)) {

      return processResponse(response);


    } catch (IOException e) {
      throw new IllegalStateException(e);
    }


  }

  private Result processResponse(CloseableHttpResponse response) throws IOException {
    String requestBody = EntityUtils.toString(getResponseEntity(response));

    return Optional.ofNullable(requestBody)
        .map(body -> gson.fromJson(body, Result.class))
        .orElseThrow(() -> new IllegalStateException(
            "Failed to extract result from response body: " + requestBody));
  }

  private HttpEntity getResponseEntity(CloseableHttpResponse response) {
    return Optional.of(response)
        .filter(this::isValid)
        .map(CloseableHttpResponse::getEntity)
        .orElseThrow(() -> new IllegalStateException(
            "No no no "
                + response));
  }


  private HttpGet prepareRequest() {
    return new HttpGet("http://localhost:4502/system/health/myapplication.json");
  }

  private boolean isValid(CloseableHttpResponse response) {
    int status = response.getStatusLine()
        .getStatusCode();
    return status >= 200 && status < 300;
  }
}