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
package com.cognifide.qa.bb.constants;

public final class HtmlTags {

  public static final String DIV = "div";

  private HtmlTags() {}

  public static final class Attributes {

    public static final String CLASS = "class";

    public static final String HREF = "href";

    public static final String VALUE = "value";

    public static final String DATA_PATH = "data-path";

    public static final String ALT = "alt";

    public static final String TITLE = "title";

    public static final String SRC = "src";

    public static final String STYLE = "style";

    private Attributes() {}
  }

  public static final class Properties {

    public static final String INNER_HTML = "innerHTML";

    public static final String OUTER_HTML = "outerHTML";

    public static final String CLASS_NAME = "className";

    private Properties() {}
  }
}
