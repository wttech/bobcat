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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.zohhak.api.Configure;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

@RunWith(ZohhakRunner.class)
@Configure(separator = "\\|")
public class NamespaceAndPathSplitterTest {

  private final NamespaceAndPathSplitter tested = new NamespaceAndPathSplitter();

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowAnExceptionIfMoreThanOneExceptionProvided() throws Exception {
    // Given
    String namespaceAndPath = "First:Second:path/to/resource";

    // When
    tested.getNamespaceAndPath(namespaceAndPath);
  }

  @TestWith({
      "Facebook : Work /            | Facebook | Work/",
      "Standard Tags/               |          | Standard Tags/",
      "Facebook : Work / Employer / | Facebook | Work/Employer/",
      "                             |          | Standard Tags/",
      "null                         |          | Standard Tags/"
  })
  public void shouldParseToProperNamespaceAndPathObjects(String givenNamespaceAndPath,
      String expectedNamespace, String expectedPath) {
    // When
    NamespaceAndPath result = tested.getNamespaceAndPath(givenNamespaceAndPath);

    // Then
    assertEquals(expectedNamespace, result.getNamespace());
    assertEquals(expectedPath, result.getPath());
  }

}
