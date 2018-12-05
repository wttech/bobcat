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
package com.cognifide.qa.bb.junit5.allure;

import static com.cognifide.qa.bb.junit5.JUnit5Constants.NAMESPACE;
import static com.cognifide.qa.bb.junit5.allure.AllureConstants.ALLURE_CREATE_ENVIROMENT;
import static com.cognifide.qa.bb.junit5.allure.AllureConstants.ALLURE_REPORT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.junit5.guice.InjectorUtils;
import com.google.inject.Injector;

/**
 * Creates a file with environment details for test report.
 * <p>
 * Loaded automatically by ServiceLoader.
 */
public class EnvironmentInfoExtension implements AfterAllCallback {

  private static final String ENVIRONMENT_FILE = "environment.properties";

  private static final org.slf4j.Logger LOG = LoggerFactory
      .getLogger(EnvironmentInfoExtension.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterAll(ExtensionContext context) {
    Injector injector = InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE);
    if (injector != null && injector.getInstance(Properties.class)
        .getProperty(ALLURE_REPORT, "false").equals("true") && injector
        .getInstance(Properties.class).getProperty(ALLURE_CREATE_ENVIROMENT, "false")
        .equals("true")) {
      prepareEnvironmentFile(injector.getInstance(Properties.class));
    }
  }

  private void prepareEnvironmentFile(Properties properties) {
    File environmentFile = new File(
        System.getProperty("allure.results.directory"), ENVIRONMENT_FILE);
    try (FileOutputStream propertiesOutputStream = new FileOutputStream(environmentFile)) {
      properties.store(propertiesOutputStream, null);
    } catch (IOException e) {
      LOG.error("Can't save properties file", e);
    }
  }
}
