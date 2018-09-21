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

import com.cognifide.qa.bb.aem.core.constants.AemConfigKeys;
import com.cognifide.qa.bb.aem.core.login.AemAuthCookieFactory;
import com.cognifide.qa.bb.aem.core.login.AemAuthCookieFactoryImpl;
import com.cognifide.qa.bb.aem.core.login.AemAuthenticationController;
import com.cognifide.qa.bb.aem.core.login.AuthorAuthenticationController;
import com.cognifide.qa.bb.aem.core.pages.AemTestPageControler;
import com.cognifide.qa.bb.aem.core.pages.sling.SlingTestPageControler;
import com.cognifide.qa.bb.aem.core.siteadmin.SiteAdminAction;
import com.cognifide.qa.bb.aem.core.siteadmin.SiteAdminController;
import com.cognifide.qa.bb.aem.core.siteadmin.aem64.AemSiteAdminController;
import com.cognifide.qa.bb.aem.core.siteadmin.aem64.CreatePageAction;
import com.cognifide.qa.bb.provider.http.HttpClientProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Module that need to be installed to use site admin actions in AEM 6.4
 */
public class AemSiteActionsModule extends AbstractModule {

  @Override
  protected void configure() {
    MapBinder<String,SiteAdminAction> siteAdminActions =
        MapBinder.newMapBinder(binder(),String.class, SiteAdminAction.class);
    siteAdminActions.addBinding(CreatePageAction.PAGE_CREATE).to(CreatePageAction.class);
  }
}
