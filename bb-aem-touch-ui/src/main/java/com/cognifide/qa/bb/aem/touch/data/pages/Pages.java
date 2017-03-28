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
package com.cognifide.qa.bb.aem.touch.data.pages;

import java.util.Map;

/**
 * Represents map of pages and their descriptions
 */
public class Pages {

  private final Map<String, PageDescription> pagesCollection;

  /**
   * Constructs Pages object.
   *
   * @param pages map containing pages descriptions.
   */
  public Pages(Map<String, PageDescription> pages) {
    pagesCollection = pages;
  }

  /**
   * @param name page name.
   * @return name of parsys.
   */
  public String getParsys(String name) {
    return pagesCollection.get(name).getParsys();
  }

  /**
   * @param name page name.
   * @return path of the page.
   */
  public String getPath(String name) {
    String path = pagesCollection.get(name).getPath();
    if (path == null) {
      throw new IllegalArgumentException("There is no path configured in pages.yaml for: " + name);
    }
    return path;
  }
}
