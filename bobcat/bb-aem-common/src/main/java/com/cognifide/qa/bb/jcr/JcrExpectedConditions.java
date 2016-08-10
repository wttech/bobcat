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
package com.cognifide.qa.bb.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * Class contains jcr ExpectedConditions for explicit waiting (provided by BobcatWait)
 */
public final class JcrExpectedConditions {

  private static final String JCR_ERROR = "Jcr error";

  private static final Logger LOG = LoggerFactory.getLogger(JcrExpectedConditions.class);

  private JcrExpectedConditions() {
  }

  /**
   * Checks if specified node exist.
   *
   * @param session  Jcr session.
   * @param nodePath Absolute path to node.
   * @return True if node exist.
   */
  public static ExpectedCondition<Boolean> nodeExist(final Session session, final String nodePath) {
    LOG.debug("Checking if node '{}' exists", nodePath);
    return input -> {
      Boolean result = null;
      try {
        session.refresh(true);
        result = session.nodeExists(nodePath);
      } catch (RepositoryException e) {
        LOG.error(JCR_ERROR, e);
      }
      return result;
    };
  }

  /**
   * Checks if node has specified property
   *
   * @param session      Jcr session.
   * @param nodePath     Absolute path to node.
   * @param propertyName Property name.
   * @return True if node has property.
   */
  public static ExpectedCondition<Boolean> hasNodeProperty(final Session session,
      final String nodePath, final String propertyName) {
    LOG.debug("Checking if node '{}' has property '{}'", nodePath, propertyName);
    return input -> {
      Boolean result = null;
      try {
        session.refresh(true);
        result = session.getNode(nodePath).hasProperty(propertyName);
      } catch (RepositoryException e) {
        LOG.error(JCR_ERROR, e);
      }
      return result;
    };
  }

  /**
   * Checks if specified node property has specified value
   *
   * @param session       Jcr session.
   * @param nodePath      Absolute path to node.
   * @param propertyName  Property name.
   * @param propertyValue Property value.
   * @return True if node property has specified value.
   */
  public static ExpectedCondition<Boolean> hasNodePropertyValue(final Session session,
      final String nodePath, final String propertyName, final String propertyValue) {
    LOG.debug("Checking if node '{}' has property '{}' with value '{}'", nodePath, propertyName,
        propertyValue);
    return input -> {
      Boolean result = null;
      try {
        session.refresh(true);
        result = session.getNode(nodePath).getProperty(propertyName).getValue().getString()
            .equals(propertyValue);
      } catch (RepositoryException e) {
        LOG.error(JCR_ERROR, e);
      }
      return result;
    };
  }
}
