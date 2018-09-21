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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

public class SlingTestDataXMLBuilderTest {

  @Test
  public void buildSlingTestData() {
    List<BasicNameValuePair> testResults = SlingTestDataXMLBuilder
        .buildSlingTestData("pageTest.xml");
    assertThat(testResults.size(), is(11));
    List<BasicNameValuePair> expectedTestData = getExpectedTestData();
    int index = 0;
    for (BasicNameValuePair testResult : testResults) {
      BasicNameValuePair expectedData = expectedTestData.get(index++);
      assertThat(testResult.getName(), is(expectedData.getName()));
      assertThat(testResult.getValue(), is(expectedData.getValue()));
    }
  }

  private List<BasicNameValuePair> getExpectedTestData() {
    List<BasicNameValuePair> toReturn = new ArrayList<>();
    toReturn.add(new BasicNameValuePair("./jcr:primaryType", "cq:Page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/cq:template",
        "/conf/we-retail/settings/wcm/templates/content-page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/jcr:description", "Test page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/jcr:primaryType", "cq:PageContent"));
    toReturn.add(new BasicNameValuePair("./jcr:content/jcr:title", "Test page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/sling:resourceType",
        "weretail/components/structure/page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/browserTitle", "Test page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/pageTitle", "Test page"));
    toReturn.add(new BasicNameValuePair("./jcr:content/image/jcr:primaryType", "nt:unstructured"));
    toReturn
        .add(new BasicNameValuePair("./jcr:content/content/jcr:primaryType", "nt:unstructured"));
    toReturn.add(new BasicNameValuePair("./jcr:content/content/sling:resourceType",
        "wcm/foundation/components/responsivegrid"));
    return toReturn;
  }
}