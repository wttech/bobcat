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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Sax parser of xml with page data.
 * It creates List<BasicNameValuePair> that contains information that can be used by page controller
 */
public class TestPageXMLParserHandler extends DefaultHandler {

  private static final String ROOT = ".";

  private static final String SEPARATOR = "/";

  private static final String[] IGNORE_ATTRIBUTES = {"xmlns"};

  private static final String IGNORE_ROOT = "jcr:root";

  private List<BasicNameValuePair> parserResults = new ArrayList<>();

  private StringBuilder entryName = new StringBuilder(ROOT);

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (!qName.equals(IGNORE_ROOT)) {
      entryName.append(SEPARATOR);
      entryName.append(qName);
    }
    for (int i = 0; i < attributes.getLength(); i++) {
      String attributeName = attributes.getQName(i);
      if (isIgnoredAttribute(attributeName)) {
        continue;
      }
      parserResults
          .add(new BasicNameValuePair(entryName.toString() + SEPARATOR + attributeName,
              attributes.getValue(attributeName)));
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (!qName.equals(IGNORE_ROOT)) {
      entryName.delete(entryName.lastIndexOf(SEPARATOR),entryName.length());
    }
  }

  private boolean isIgnoredAttribute(String attributeName) {
    return Arrays.stream(IGNORE_ATTRIBUTES).anyMatch(s ->
        s.equals(attributeName) || attributeName.startsWith(s));
  }


  /**
   * @return list of information about page to create.
   */
  public List<BasicNameValuePair> getParserResults() {
    return parserResults;
  }
}
