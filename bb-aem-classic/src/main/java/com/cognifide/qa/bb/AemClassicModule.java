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
package com.cognifide.qa.bb;

import org.apache.http.impl.client.CloseableHttpClient;

import com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfigurerFactory;
import com.cognifide.qa.bb.aem.ui.DialogFieldProvider;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.mapper.field.FieldProvider;
import com.cognifide.qa.bb.provider.http.HttpClientProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;

/**
 * This module contains:
 * <ul>
 * <li>binding DialogField annotation,
 * <li>factory for draggable and droppable elements.
 * </ul>
 */
public class AemClassicModule extends AbstractModule {

  /**
   * Provider method that produces a CloseableHttpClient instances configured with URL, login and password
   * taken from property files.
   *
   * @param url      IP address of Author instance, automatically set to author.ip property.
   * @param login    Author login, automatically set to author.login property.
   * @param password Author password, automatically set to author.password property.
   * @return New instance of CloseableHttpClient.
   */
  @Provides
  public CloseableHttpClient getAuthorHttpClient(@Named(AemConfigKeys.AUTHOR_IP) String url,
      @Named(AemConfigKeys.AUTHOR_LOGIN) String login,
      @Named(AemConfigKeys.AUTHOR_PASSWORD) String password) {
    return new HttpClientProvider(login, password, url).get();
  }

  @Override
  protected void configure() {
    Multibinder<FieldProvider> uriBinder = Multibinder.newSetBinder(binder(), FieldProvider.class);
    uriBinder.addBinding().to(DialogFieldProvider.class);

    install(new FactoryModuleBuilder().build(ComponentConfigurerFactory.class));
  }
}
