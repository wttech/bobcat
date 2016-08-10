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
package com.cognifide.qa.bb.aem.dialog.classic.field.tags;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a utility class that is able to construct a NamespaceAndPath instance out of textual
 * description of a tag.
 */
class NamespaceAndPathSplitter {

  private static final String WHITESPACES_REGEXP = "(\\s*)";

  /**
   * Parses namespace and path string separated by ':' into NamespaceAndPath object.
   *
   * @return NamespaceAndPath instance created out of textual description of a tag.
   */
  NamespaceAndPath getNamespaceAndPath(String namespaceAndPathText) {
    String nonEmptyText = StringUtils.defaultString(namespaceAndPathText);
    NamespaceAndPathBuilder result = new NamespaceAndPathBuilder();
    String[] parts = nonEmptyText.split(AemTags.NAMESPACE_SEPARATOR);
    if (parts.length > 2) {
      throw new IllegalArgumentException("Invalid tag");
    } else if (parts.length == 2) {
      result.setNamespace(trimWhiteSpaces(parts[0]));
      result.setPath(trimWhiteSpaces(parts[1]));
    } else if (parts.length == 1) {
      result.setPath(trimWhiteSpaces(parts[0]));
    }
    return result.build();
  }

  private String trimWhiteSpaces(String text) {
    String separatorWithWhitespaces =
        WHITESPACES_REGEXP + AemTags.PARENT_PATH_SEPARATOR + WHITESPACES_REGEXP;
    return text.trim().replaceAll(separatorWithWhitespaces, AemTags.PARENT_PATH_SEPARATOR);
  }
}
