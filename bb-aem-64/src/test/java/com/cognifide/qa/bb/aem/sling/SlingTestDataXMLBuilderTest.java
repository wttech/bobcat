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
package com.cognifide.qa.bb.aem.sling;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.cognifide.qa.bb.aem.core.pages.sling.SlingDataXMLBuilder;

public class SlingTestDataXMLBuilderTest {

  private static final String TEST_PAGE = "Test page";

  @Test
  public void buildSlingTestData() {
    List<BasicNameValuePair> testResults = SlingDataXMLBuilder
        .buildFromFile("pageTest.xml");
    Assertions.assertThat(testResults.size()).isEqualTo(11);
    List<BasicNameValuePair> expectedTestData = getExpectedTestData();
    int index = 0;
    for (BasicNameValuePair testResult : testResults) {
      BasicNameValuePair expectedData = expectedTestData.get(index++);
      Assertions.assertThat(testResult.getName()).isEqualTo(expectedData.getName());
      Assertions.assertThat(testResult.getValue()).isEqualTo(expectedData.getValue());
    }
  }

  private List<BasicNameValuePair> getExpectedTestData() {
    List<BasicNameValuePair> toReturn = new ArrayList<>();
    toReturn.add(new BasicNameValuePair("./jcr:primaryType", "cq:Page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/cq:template",
        "/conf/we-retail/settings/wcm/templates/content-page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/jcr:description", TEST_PAGE));
    toReturn.add(new BasicNameValuePair("./jcr:content/jcr:primaryType", "cq:PageContent"));
    toReturn.add(new BasicNameValuePair("./jcr:content/jcr:title", TEST_PAGE));
    toReturn.add(new BasicNameValuePair("./jcr:content/sling:resourceType",
        "weretail/components/structure/page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/browserTitle", TEST_PAGE));
    toReturn.add(new BasicNameValuePair("./jcr:content/pageTitle", TEST_PAGE));
    toReturn.add(new BasicNameValuePair("./jcr:content/image/jcr:primaryType", "nt:unstructured"));
    toReturn
        .add(new BasicNameValuePair("./jcr:content/content/jcr:primaryType", "nt:unstructured"));
    toReturn.add(new BasicNameValuePair("./jcr:content/content/sling:resourceType",
        "wcm/foundation/components/responsivegrid"));
    return toReturn;
  }
}
