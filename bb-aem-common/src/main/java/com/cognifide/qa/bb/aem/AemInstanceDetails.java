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
package com.cognifide.qa.bb.aem;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * This class provides information about AEM instance version described in property files.
 */
@Singleton
public class AemInstanceDetails {

  private static final Logger LOG = LoggerFactory.getLogger(AemInstanceDetails.class);

  @Inject
  @Named(AemConfigKeys.AUTHOR_IP)
  private String authorIp;

  @Inject
  private CloseableHttpClient httpClient;

  private DefaultArtifactVersion aemVersion;

  /**
   * @return Version of author instance.
   */
  public synchronized DefaultArtifactVersion getAemVersion() {
    if (aemVersion == null) {
      String versionInfo = sendInfoRequests();
      aemVersion = parseVersionInfo(versionInfo);
    }
    return aemVersion;
  }

  private String sendInfoRequests() {
    HttpGet infoGet = new HttpGet(authorIp + "/libs/cq/core/productinfo.json");
    String resultJson = "";
    try (CloseableHttpResponse infoResponse = httpClient.execute(infoGet)) {
        resultJson = EntityUtils.toString(infoResponse.getEntity());
    } catch (IOException e) {
      LOG.error("Can't get aem version", e);
    } finally {
      infoGet.reset();
    }

    return resultJson;
  }

  private DefaultArtifactVersion parseVersionInfo(String versionInfo) {
    Gson gson = new Gson();
    JsonElement element = gson.fromJson(versionInfo, JsonElement.class);
    JsonObject jsonObj = element.getAsJsonObject();
    aemVersion = new DefaultArtifactVersion(jsonObj.get("version").getAsString());

    return aemVersion;
  }
}
