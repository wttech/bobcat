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
package com.cognifide.qa.bb.proxy.record.har.predicates;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Predicate;

import net.lightbody.bmp.core.har.HarEntry;

/**
 * Predicate that matches HarEntries with matching URL path prefix
 */
public class PathPrefixPredicate implements Predicate<HarEntry> {

  private final String pathPrefix;

  /**
   * Constructor. Initializes PathPrefixPredicate with pathPrefix.
   *
   * @param pathPrefix path prefix
   */
  public PathPrefixPredicate(String pathPrefix) {
    this.pathPrefix = pathPrefix;
  }

  @Override
  public boolean apply(HarEntry harEntry) {
    URL url;
    String urlString = harEntry.getRequest().getUrl();
    try {
      url = new URL(urlString);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(String.format("Malformed URL '%s'", urlString), e);
    }
    String path = url.getPath();
    return path.startsWith(pathPrefix);
  }

  @Override
  public String toString() {
    return String.format("PathPrefixPredicate(resource path starts with %s)", this.pathPrefix);
  }

}
