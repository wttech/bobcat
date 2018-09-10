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
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Main module that need to be installed to use AEM functions
 */
public class AemCoreModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AemTestPageControler.class).annotatedWith(ControlWithSling.class).to(
        SlingTestPageControler.class);
    bind(AemAuthenticationController.class).annotatedWith(AuthorInstance.class).to(
        AuthorAuthenticationController.class);
    bind(AemAuthCookieFactory.class).annotatedWith(AuthorInstance.class).to(
        AemAuthCookieFactoryImpl.class);
    bind(SiteAdminController.class).annotatedWith(Aem64.class).to(AemSiteAdminController.class);

    bindActions64();
  }

  private void bindActions64() {
    Multibinder<SiteAdminAction> siteAdminActions64 =
        Multibinder.newSetBinder(binder(), SiteAdminAction.class, Aem64.class);
    siteAdminActions64.addBinding().to(CreatePageAction.class);
  }

  @Provides
  @AuthorInstance
  public CloseableHttpClient getAuthorHttpClient(@Named(AemConfigKeys.AUTHOR_IP) String url,
      @Named(AemConfigKeys.AUTHOR_LOGIN) String login,
      @Named(AemConfigKeys.AUTHOR_PASSWORD) String password) {
    return new HttpClientProvider(login, password, url).get();
  }
}
