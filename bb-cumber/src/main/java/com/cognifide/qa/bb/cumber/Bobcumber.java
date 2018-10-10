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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.ConfigKeys;
import com.cognifide.qa.bb.cumber.rerun.FailedTestsRunner;
import com.cognifide.qa.bb.cumber.rerun.TooManyTestsToRerunException;
import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverRegistry;
import com.cognifide.qa.bb.utils.PropertyUtils;

import cucumber.api.java.hu.De;
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

  private final Properties properties = PropertyUtils.gatherProperties();

  private final double maxFailedTestPercentage = Double.parseDouble(properties.getProperty(ConfigKeys.BOBCAT_REPORT_STATISTICS_PERCENTAGE));

  private final StatisticsHelper statisticsHelper;

  private final boolean storeFailedResults;

  private final boolean isItFailedTestsRerun;

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
    statisticsHelper = new StatisticsHelper();
    storeFailedResults = clazz.isAnnotationPresent(StoreFailedResults.class);
    if (storeFailedResults) {
      featureFile = createFile(clazz.getAnnotation(StoreFailedResults.class).value());
      String statisticsFilePath = properties.getProperty(ConfigKeys.BOBCAT_REPORT_STATISTICS_PATH);
      statisticsFile = createFile(statisticsFilePath);
    }
    isItFailedTestsRerun = clazz.isAnnotationPresent(FailedTestsRunner.class);
  }

  @Override
  public void run(RunNotifier notifier) {
    if (isItFailedTestsRerun && !canRerunFailedTests()) {
      shutdownRerun(notifier);
    } else {
      if (storeFailedResults) {
        notifier.addListener(new BobcumberListener(this));
      }
      super.run(notifier);
      closeWebDriverPool();
    }
  }

  private File createFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
    File file = new File(path);
    if (!file.exists()) {
      PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8.name());
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

  private void shutdownRerun(RunNotifier notifier) {
    double percentageOfFailedTests = statisticsHelper.getPercentageOfFailedTests(statisticsFile);
    int failedTestsNumber = statisticsHelper.getNumberOfFailedTests(statisticsFile);
    if (failedTestsNumber == 0) {
      notifier.fireTestFinished(Description.EMPTY);
    } else if (percentageOfFailedTests > maxFailedTestPercentage) {
      String failureMessage = "Percentage of failed tests was bigger than " + maxFailedTestPercentage + ".";
      Failure failure = new Failure(Description.createSuiteDescription(failureMessage),
          new TooManyTestsToRerunException(failureMessage));
      notifier.fireTestFailure(failure);
    }
  }

  private boolean canRerunFailedTests() {
    int failedTestsNumber = statisticsHelper.getNumberOfFailedTests(statisticsFile);
    double percentageOfFailedTests = statisticsHelper.getPercentageOfFailedTests(statisticsFile);
    boolean haveNoTests = failedTestsNumber == 0;
    boolean haveTooManyTests = percentageOfFailedTests > maxFailedTestPercentage;
    return !haveNoTests && !haveTooManyTests;
  }

  @Deprecated
  File getFeatureFile() {
    return featureFile;
  }

  @Deprecated
  File getStatisticsFile() {
    return statisticsFile;
  }
}
