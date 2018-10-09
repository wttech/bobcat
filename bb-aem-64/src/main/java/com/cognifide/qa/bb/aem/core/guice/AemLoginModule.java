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
package com.cognifide.qa.bb.aem.core.guice;

import com.cognifide.qa.bb.aem.core.login.AemAuthCookieFactory;
import com.cognifide.qa.bb.aem.core.login.AemAuthCookieFactoryImpl;
import com.cognifide.qa.bb.aem.core.login.AemAuthenticationController;
import com.cognifide.qa.bb.aem.core.login.AuthorAuthenticationController;
import com.cognifide.qa.bb.aem.core.siteadmin.SiteAdminController;
import com.cognifide.qa.bb.aem.core.siteadmin.AemSiteAdminController;
import com.google.inject.AbstractModule;

/**
 * Module that need to be installed to use login to AEM functions
 */
public class AemLoginModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AemAuthenticationController.class).to(
        AuthorAuthenticationController.class);
    bind(AemAuthCookieFactory.class).to(
        AemAuthCookieFactoryImpl.class);
    bind(SiteAdminController.class).to(AemSiteAdminController.class);
  }
}
