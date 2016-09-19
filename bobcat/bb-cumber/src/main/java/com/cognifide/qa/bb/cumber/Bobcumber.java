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
package com.cognifide.qa.bb.cumber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Properties;

import com.google.inject.Inject;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.ConfigKeys;
import com.cognifide.qa.bb.PropertyBinder;
import com.cognifide.qa.bb.cumber.rerun.FailedTestsRunner;
import com.cognifide.qa.bb.cumber.rerun.TooManyTestsException;
import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverRegistry;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.Backend;
import cucumber.runtime.Runtime;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.guice.impl.GuiceFactory;

/**
 * Classes annotated with {@code @RunWith(Bobcumber.class)} will run a Cucumber Feature
 */
public class Bobcumber extends Cucumber {

  private static final Logger LOG = LoggerFactory.getLogger(Bobcumber.class);

  private boolean storeFailedResults;

  private boolean isItFailedTestsRerun;

  private String statisticsFilePath;

  private Integer maxFailedTestPercentage;

  @Inject
  private Properties properties;

  private File featureFile;

  private File statisticsFile;

  /**
   * Constructor called by JUnit.
   *
   * @param clazz the class with the @RunWith annotation.
   * @throws java.io.IOException                         if there is a problem
   * @throws org.junit.runners.model.InitializationError if there is another problem
   */
  public Bobcumber(Class<?> clazz) throws InitializationError, IOException {
    super(clazz);
    bindFields();
    storeFailedResults = clazz.isAnnotationPresent(StoreFailedResults.class);
    if (storeFailedResults) {
      featureFile = createFile(clazz.getAnnotation(StoreFailedResults.class).value());
      statisticsFile = createFile(statisticsFilePath);
    }
    isItFailedTestsRerun = clazz.isAnnotationPresent(FailedTestsRunner.class);
  }

  @Override
  public void run(RunNotifier notifier) {
    if (isItFailedTestsRerun) {
      try {
        if (StatisticsUtils.getNumberOfFailedTests(statisticsFile).equals(0)) {
          notifier.fireTestFinished(Description.EMPTY);
          return;
        }
        if (StatisticsUtils.getPercentageOfFailedTests(statisticsFile) > maxFailedTestPercentage) {
          notifier.fireTestFailure(new Failure(Description.createSuiteDescription("Too many tests."), new TooManyTestsException()));
          return;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (storeFailedResults) {
      notifier.addListener(new BobcumberListener(this));
    }
    super.run(notifier);
    closeWebDriverPool();
  }

  private File createFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
    File file = new File(path);
    if (!file.exists()) {
      PrintWriter writer = new PrintWriter(file, CharEncoding.UTF_8);
      writer.close();
    }
    return file;
  }

  @SuppressWarnings("unchecked")
  private void closeWebDriverPool() {
    try {
      Runtime runtime = (Runtime) FieldUtils.readField(this, "runtime", true);
      Collection<? extends Backend> backends =
          (Collection<? extends Backend>) FieldUtils.readField(runtime, "backends", true);
      for (Backend backend : backends) {
        if (backend instanceof JavaBackend) {
          GuiceFactory factory =
              (GuiceFactory) FieldUtils.readField(backend, "objectFactory", true);
          WebDriverRegistry webDriverRegistry = factory.getInstance(WebDriverRegistry.class);
          webDriverRegistry.shutdown();
        }
      }
    } catch (IllegalAccessException e) {
      LOG.error("unable to close web driver pool", e);
    }
  }

  private void bindFields() {
    properties = bindProperties();
    statisticsFilePath = properties.getProperty(ConfigKeys.BOBCAT_REPORT_STATISTICS_PATH);
    maxFailedTestPercentage = Integer.parseInt(properties.getProperty(ConfigKeys.BOBCAT_REPORT_STATISTICS_PERCENTAGE));
  }

  private Properties bindProperties() {
    String parents = System.getProperty(com.cognifide.qa.bb.constants.ConfigKeys.CONFIGURATION_PATHS, "src/main/config/common");
    String[] split = StringUtils.split(parents, ";");
    Properties properties = loadDefaultProperties();
    for (String name : split) {
      File configParent = new File(StringUtils.trim(name));
      try {
        loadProperties(configParent, properties);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return properties;
  }

  private static void loadProperties(File file, Properties properties) throws IOException {
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        loadProperties(child, properties);
      }
    } else {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

      LOG.debug("loading properties from: {} (ie. {})", file, file.getAbsolutePath());
      try {
        properties.load(reader);
      } finally {
        reader.close();
      }
    }
  }

  private static Properties loadDefaultProperties() {
    Properties properties = new Properties();
    try (InputStream is = PropertyBinder.class.getClassLoader()
        .getResourceAsStream(com.cognifide.qa.bb.constants.ConfigKeys.DEFAULT_PROPERTIES_NAME)) {
      properties.load(is);
    } catch (IOException e) {
      LOG.error("Can't bind default properties", e);
    }
    return properties;
  }

  public File getFeatureFile() {
    return featureFile;
  }

  public File getStatisticsFile() {
    return statisticsFile;
  }
}
