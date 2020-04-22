/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.journeys.modules;

import com.cognifide.qa.bb.journeys.docs.Content;
import com.cognifide.qa.bb.journeys.docs.Footer;
import com.cognifide.qa.bb.journeys.docs.Masthead;
import com.cognifide.qa.bb.journeys.docs.Nav;
import com.cognifide.qa.bb.journeys.docs.homepage.xpath.ContentXpath;
import com.cognifide.qa.bb.journeys.docs.homepage.xpath.FooterXpath;
import com.cognifide.qa.bb.journeys.docs.homepage.xpath.MastheadXpath;
import com.cognifide.qa.bb.journeys.docs.homepage.xpath.NavXpath;
import com.google.inject.AbstractModule;

public class HomepageXpath extends AbstractModule {
  @Override
  protected void configure() {
    bind(Masthead.class).to(MastheadXpath.class);
    bind(Content.class).to(ContentXpath.class);
    bind(Footer.class).to(FooterXpath.class);
    bind(Nav.class).to(NavXpath.class);
  }
}
