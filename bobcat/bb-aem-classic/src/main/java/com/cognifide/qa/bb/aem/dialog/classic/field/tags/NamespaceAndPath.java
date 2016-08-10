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

/**
 * This class contains information about a particular AemTag.
 */
final class NamespaceAndPath {

  private final String namespace;

  private final String path;

  NamespaceAndPath(String namespace, String path) {
    this.namespace = namespace;
    this.path = path;
  }

  /**
   * @return Namespace.
   */
  String getNamespace() {
    return namespace;
  }

  /**
   * @return Path.
   */
  String getPath() {
    return path;
  }

  @Override
  public String toString() {
    if (!"".equals(namespace)) {
      return getNamespace() + AemTags.NAMESPACE_SEPARATOR + getPath();
    } else {
      return getPath();
    }
  }

}
