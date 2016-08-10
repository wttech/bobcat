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
package com.cognifide.qa.bb.provider.jcr.session;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.provider.jcr.properties.InstanceProperties;
import com.google.inject.Provider;

/**
 * Provides Jcr Session for desired environment
 */
@ThreadScoped
public class JcrSessionProvider implements Provider<Session> {

  private static final Logger LOG = LoggerFactory.getLogger(JcrSessionProvider.class);

  private final InstanceProperties properties;

  private Session session;

  /**
   * Constructor. Initializes JcrSessionProvider
   *
   * @param instanceProperties AEM instance properties.
   */
  public JcrSessionProvider(InstanceProperties instanceProperties) {
    this.properties = instanceProperties;
  }

  @Override
  public Session get() {
    if (session == null || !session.isLive()) {
      try {
        Repository repository = JcrUtils.getRepository(properties.getIp() + "/crx/server");
        SimpleCredentials credentials = new SimpleCredentials(properties.getLogin(),
            properties.getPassword().toCharArray());
        session = repository.login(credentials);
      } catch (RepositoryException e) {
        LOG.error("Can't connect with jcr repository", e);
      }
    }
    return session;
  }
}
