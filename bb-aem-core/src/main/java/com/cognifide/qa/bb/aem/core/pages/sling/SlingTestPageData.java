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

import com.cognifide.qa.bb.aem.core.pages.TestPageData;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

/**
 * Page data that can be used by {@link SlingTestPageControler}
 */
public class SlingTestPageData implements TestPageData {

  private String contentPath;

  private List<BasicNameValuePair> content = new ArrayList<>();

  /**
   * @param contentPath path where page (list of nodes) should be created
   * @param content list of nodes to be created
   */
  public SlingTestPageData(String contentPath,
      List<BasicNameValuePair> content) {
    this.contentPath = contentPath;
    this.content = content;
  }

  /**
   * @return path where page (list of nodes) should be created
   */
  public String getContentPath() {
    return contentPath;
  }

  /**
   * @return  list of nodes to be created
   */
  public List<BasicNameValuePair> getContent() {
    return content;
  }


}
