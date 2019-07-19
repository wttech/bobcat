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

import com.cognifide.qa.bb.aem.core.modules.fields.DialogFieldsModule;
import com.google.inject.AbstractModule;

/**
 * Main module that needs to be installed to use AEM 6.5 functions.
 * <p>
 * It install all sub-modules related to AEM 6.5.
 */
public class Aem65FullModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(Aem65FullModule.class);

  @Override
  protected void configure() {
    LOG.debug("Configuring Bobcat module: {}", getClass().getSimpleName());

    install(new AemCoreModule());
    install(new AemLoginModule());
    install(new AemSitesAdminModule());
    install(new SlingPageActionsModule());
    install(new AemComponentModule());
    install(new AemSidePanelModule());
    install(new AemPageModule());
    install(new DialogFieldsModule());
    install(new AemConfigModule());
  }

}
