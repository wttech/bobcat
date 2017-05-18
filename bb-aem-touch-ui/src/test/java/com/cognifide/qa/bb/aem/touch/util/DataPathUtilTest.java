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
package com.cognifide.qa.bb.aem.touch.util;

import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

@RunWith(ZohhakRunner.class)
public class DataPathUtilTest {

  @TestWith({
      "/datapath,                                               /datapath",
      "/datapath_1234,                                          /datapath",
      "/content/box_384262182_copy/content/richtext_1129363668, /content/box_384262182_copy/content/richtext"}
  )
  public void shouldNormalizeDataPathContainingNumber(String testedDataPath, String expected) {
    String actual = DataPathUtil.normalize(testedDataPath);

    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @TestWith({
      "/content/some/path/jcr:content/a/nested/richtext,    /a/nested/richtext",
      "/content/some/path/jcr:content/richtext_1129363668,  /richtext_1129363668"}
  )
  public void shouldExtractOnlyTheDataPathOnlyFromTheWholePath(String testedDataPath, String expected) {
    String actual = DataPathUtil.extract(testedDataPath);

    Assertions.assertThat(actual).isEqualTo(expected);
  }

}
