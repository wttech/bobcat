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

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.cognifide.qa.bb.api.actions.ActionData;

/**
 * {@link ActionData} that can be used by page actions executed via Sling API.
 */
public class SlingPageData implements ActionData {

  private String contentPath;

  private List<BasicNameValuePair> content;

  /**
   * @param contentPath path where page (list of nodes) should be created
   * @param content     list of nodes to be created
   */
  public SlingPageData(String contentPath, List<BasicNameValuePair> content) {
    this.contentPath = contentPath;
    this.content = content;
  }

  /**
   * @param contentPath path where page (list of nodes) should be created
   */
  public SlingPageData(String contentPath) {
    this.contentPath = contentPath;
  }

  /**
   * @return path where page (list of nodes) should be created
   */
  public String getContentPath() {
    return contentPath;
  }

  /**
   * @return list of nodes to be created
   */
  public List<BasicNameValuePair> getContent() {
    return content;
  }

}
