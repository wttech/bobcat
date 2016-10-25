/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.quarantine;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RemoteQuarantineManager implements QuarantineManager {

  //@formatter:off
  private final Type responseType = new TypeToken<List<QuarantinedElement>>() {}.getType();
  //@formatter:on

  private final Gson gson;

  private final CloseableHttpClient httpClient;

  public RemoteQuarantineManager() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
      public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        return new Date(json.getAsJsonPrimitive().getAsLong());
      }
    });

    this.gson = gsonBuilder.create();
    this.httpClient = HttpClients.createDefault();
  }

  @Override
  public List<QuarantinedElement> getQuarantinedElements(String url) throws TestQuarantineEception {
    HttpGet httpget = new HttpGet(url);
    try (CloseableHttpResponse response = httpClient.execute(httpget)) {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        Reader reader = new InputStreamReader(entity.getContent(), charset);
        return gson.fromJson(reader, responseType);
      }
    } catch (IOException ex) {
      throw new TestQuarantineEception("Not able to retreive quarantined elements form URL.", ex);
    }
    return Collections.emptyList();
  }

  @Override
  public void putElementToQuarantine(String url, QuarantinedElement element)
      throws TestQuarantineEception {
    HttpPost httpPost = new HttpPost(url);
    HttpEntity entity = new ByteArrayEntity(gson.toJson(element).getBytes(StandardCharsets.UTF_8));
    httpPost.setEntity(entity);
    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
      if (response.getStatusLine().getStatusCode() >= 400) {
        String resposneMessage = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        throw new TestQuarantineEception(
            "Not able to add a test to quarantine: " + resposneMessage);
      }
    } catch (IOException ex) {
      throw new TestQuarantineEception("Not able to add a test to quarantine!", ex);
    }
  }

  @Override
  public void removeElementFromQuarantine(String url, QuarantinedElement element)
      throws TestQuarantineEception {

    HttpDelete httpDelete = new HttpDelete(url);
    try {
      URI uri = new URIBuilder(httpDelete.getURI())
          .setParameter("testClass", element.getFeatureName())
          .setParameter("testMethod", element.getScenarioName())
          .build();
      httpDelete.setURI(uri);
    } catch (URISyntaxException ex) {
      throw new TestQuarantineEception("Not able to add a test to quarantine!", ex);
    }
    try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
      if (response.getStatusLine().getStatusCode() >= 400) {
        String resposneMessage = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        throw new TestQuarantineEception(
            "Not able to add a test to quarantine: " + resposneMessage);
      }
    } catch (IOException ex) {
      throw new TestQuarantineEception("Not able to add a test to quarantine!", ex);
    }
  }

}
