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
package com.cognifide.qa.bb.aem.core.pages.sling;

import com.cognifide.qa.bb.aem.core.constants.AemConfigKeys;
import com.cognifide.qa.bb.aem.core.pages.AemPageManipulationException;
import com.cognifide.qa.bb.aem.core.pages.AemTestPageControler;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Collections;
import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * Implementation of {@link AemTestPageControler} that uses Sling
 */
public class SlingTestPageControler implements AemTestPageControler<SlingTestPageData> {

  @Inject
  private CloseableHttpClient httpClient;

  @Inject
  @Named(AemConfigKeys.AUTHOR_IP)
  private String authorIP;

  /**
   * @param testPageData data for page creation
   */
  @Override
  @Step("Create test page {testPageData.contentPath}")
  public void createTestPage(SlingTestPageData testPageData) throws AemPageManipulationException {
    HttpPost request = new HttpPost(authorIP + testPageData.getContentPath());
    request.setEntity(new UrlEncodedFormEntity(testPageData.getContent(), Consts.UTF_8));
    try {
      httpClient.execute(request);
    } catch (IOException e) {
      throw new AemPageManipulationException(e);
    }

  }

  /**
   * @param testPageData information about page to remove
   */
  @Override
  @Step("Delete test page {testPageData.contentPath}")
  public void deleteTestPage(SlingTestPageData testPageData) throws AemPageManipulationException {
    HttpPost request = new HttpPost(authorIP + testPageData.getContentPath());
    request.setEntity(new UrlEncodedFormEntity(
        Collections.singleton(new BasicNameValuePair(":operation", "delete")), Consts.UTF_8));

    try {
      httpClient.execute(request);
    } catch (IOException e) {
      throw new AemPageManipulationException(e);
    }
  }
}
