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
import java.util.Collection;

import com.cognifide.qa.bb.cumber.rerun.FailedTestsRunner;
import com.cognifide.qa.bb.cumber.rerun.TooManyTestsException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverRegistry;
import com.google.inject.Inject;
import com.google.inject.name.Named;

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

  //TODO: fix null injection
  @Inject
  @Named("bobcat.report.statisticsFilePath")
  private String statisticsFilePath = "target/testsStatisticFile.properties";

  //TODO: fix null injection
  @Inject
  @Named("bobcat.report.maxFailedTestPercentage")
  private Integer maxFailedTestPercentage = 30;

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
          notifier.fireTestFailure(new Failure(null, new TooManyTestsException()));
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

  public File getFeatureFile() {
    return featureFile;
  }

  public File getStatisticsFile() {
    return statisticsFile;
  }
}
