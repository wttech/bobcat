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
 * This class produces a hacked NamespaceAndPath instance.
 * <p>
 * For default namespace each tag should begin with Standard Tags. Unfortunately this is only done after
 * form submit. This is kind of a hack to be able to search that tag by both way: Standard Tags/MyTagName
 * and MyTagName.
 */
class NamespaceAndPathBuilder {

  /**
   * Default namespace for AemTags.
   */
  private static final String DEFAULT_PARENT_FOR_NO_NAMESPACE_ITEMS = "Standard Tags"
      + AemTags.PARENT_PATH_SEPARATOR;

  private String namespace;

  private String path;

  /**
   * Sets the namespace.
   *
   * @param namespace
   * @return This builder.
   */
  NamespaceAndPathBuilder setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the path.
   *
   * @param path
   * @return This builder.
   */
  NamespaceAndPathBuilder setPath(String path) {
    this.path = path;
    return this;
  }

  /**
   * @return Hacked NamespaceAndPath instance. See class description for explanation.
   */
  NamespaceAndPath build() {
    namespace = StringUtils.defaultString(namespace);
    path = StringUtils.defaultString(path);

    if ("".equals(namespace) && !path.startsWith(DEFAULT_PARENT_FOR_NO_NAMESPACE_ITEMS)) {
      path = DEFAULT_PARENT_FOR_NO_NAMESPACE_ITEMS + path;
    }
    return new NamespaceAndPath(namespace, path);
  }
}
