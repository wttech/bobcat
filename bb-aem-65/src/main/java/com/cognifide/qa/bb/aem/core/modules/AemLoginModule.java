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
package com.cognifide.qa.bb.aem.core.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.core.api.AemActions;
import com.cognifide.qa.bb.aem.core.login.AemAuthCookieFactory;
import com.cognifide.qa.bb.aem.core.login.AemAuthCookieFactoryImpl;
import com.cognifide.qa.bb.aem.core.login.actions.LogIn;
import com.cognifide.qa.bb.aem.core.login.actions.LogOut;
import com.cognifide.qa.bb.api.actions.Action;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Module that contains binding related to login to AEM instance functionality
 */
public class AemLoginModule extends AbstractModule {
  private static final Logger LOG = LoggerFactory.getLogger(AemLoginModule.class);

  @Override
  protected void configure() {
    LOG.debug("Configuring Bobcat module: {}", getClass().getSimpleName());

    bind(AemAuthCookieFactory.class).to(AemAuthCookieFactoryImpl.class);

    MapBinder<String, Action> siteAdminActions =
        MapBinder.newMapBinder(binder(), String.class, Action.class);
    siteAdminActions.addBinding(AemActions.LOG_IN).to(LogIn.class);
    siteAdminActions.addBinding(AemActions.LOG_OUT).to(LogOut.class);
  }
}
