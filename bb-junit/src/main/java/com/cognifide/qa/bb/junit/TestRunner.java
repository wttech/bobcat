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
package com.cognifide.qa.bb.junit;

import java.util.List;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.junit.listener.ReportingListener;
import com.cognifide.qa.bb.qualifier.Retry;
import com.google.inject.Injector;

/**
 * <p>
 * This JUnit test runner allows to run the test in a Guice context. The modules defining the
 * context are defined with the {@link Modules} annotation.
 * </p>
 * 
 * <pre>
 * {@literal @}RunWith(TestRunner.class) {@literal @}Modules(MyModule.class)
 * public class MyTest {
 *
 *   {@literal @}Inject private MyClass myClass;
 *
 *   {@literal @}Test
 *   public void testWithGuice() {
 *     assertNotNull("Guice works!", myClass);
 *   }
 * }
 * </pre>
 */
public class TestRunner extends BlockJUnit4ClassRunner {

  private static final Logger LOG = LoggerFactory.getLogger(TestRunner.class);

  private static final ReportingListener reportingListener = new ReportingListener();

  private static volatile boolean isReportingListenerRegistered = false;

  private final Injector injector;

  private final Properties properties;

  private final TestEventCollector testEventCollector;

  /**
   * Creates a Runner with Guice modules.
   *
   * @param classToRun the test class to run
   * @throws InitializationError if the test class is malformed
   * @throws IllegalAccessException if the test class is malformed
   * @throws InstantiationException if the test class is malformed
   */
  public TestRunner(final Class<?> classToRun)
      throws InitializationError, InstantiationException, IllegalAccessException {
    super(classToRun);
    injector = InjectorsMap.INSTANCE.forClass(classToRun);
    properties = injector.getInstance(Properties.class);
    testEventCollector = injector.getBinding(TestEventCollector.class).getProvider().get();
    reportingListener.addInjector(injector);
  }

  /**
   * @return Instance of the test class, created using Guice.
   */
  @Override
  public final Object createTest() {
    return injector.getInstance(getTestClass().getJavaClass());
  }

  /**
   * <p>
   * This method runs the test. It was overridden in order to add the test interceptor.
   * </p>
   * <p>
   * This method check two annotations before invoking the test method.
   * </p>
   * <ol>
   * <li>Checks {@link Ignore} annotation. If exists the test is ignored.</li>
   * <li>Invoke beforeTestRun method of the test interceptor.</li>
   * <li>Invoke the test method: run the test.</li>
   * <li>Invoke afterTestRun method of the test interceptor.</li>
   * </ol>
   *
   * @param method - framework method
   * @param notifier - run notifier
   */
  @Override
  protected final void runChild(FrameworkMethod method, RunNotifier notifier) {
    EachTestNotifier eachNotifier = makeNotifier(method, notifier);
    if (method.getAnnotation(Ignore.class) != null) {
      eachNotifier.fireTestIgnored();
      return;
    }

    eachNotifier.fireTestStarted();
    try {
      runMethod(method);
    } catch (AssumptionViolatedException e) {
      eachNotifier.addFailedAssumption(e);
    } catch (Throwable e) {
      eachNotifier.addFailure(e);
    } finally {
      eachNotifier.fireTestFinished();
    }
  }

  @Override
  public void run(RunNotifier notifier) {
    checkAndRegisterReportingListener(notifier);
    super.run(notifier);
  }

  @Override
  protected List<TestRule> getTestRules(Object target) {
    LOG.debug("adding additional rules for test: '{}'", target);
    List<TestRule> result = super.getTestRules(target);
    result.add(new ReportingRule(injector));
    result.add(new WebDriverClosingRule(injector));
    return result;
  }

  /*
   * Guice can inject constructors with parameters so we don't want this method to trigger an error
   *
   */
  @Override
  protected final void validateZeroArgConstructor(List<Throwable> errors) {
    // empty
  }

  private void runMethod(FrameworkMethod method) throws Throwable {
    int runs = 0;
    int retryCount = retrieveRetry(method);
    boolean testFailed = true;
    while (testFailed) {
      try {
        methodBlock(method).evaluate();
        testFailed = false;
      } catch (AssumptionViolatedException e) {
        throw e;
      } catch (Throwable e) {
        if (runs >= retryCount) {
          throw e;
        }
        testEventCollector.removeLastEntry();
      }
      runs++;
    }
    if (runs > 1) {
      testEventCollector.info("Test passed in: " + runs + " attempt");
    }
  }

  private int retrieveRetry(FrameworkMethod method) {
    Retry retry = method.getAnnotation(Retry.class);
    return retry != null ? calculateRetryCount(retry.reruns()) : 0;
  }

  private int calculateRetryCount(int reruns) {
    return reruns > 0 ? reruns
        : Integer.parseInt(properties.getProperty(ConfigKeys.JUNIT_RERUNS, "0"));
  }

  private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
    Description description = describeChild(method);
    return new EachTestNotifier(notifier, description);
  }

  private void checkAndRegisterReportingListener(RunNotifier notifier) {
    synchronized (TestRunner.class) {
      if (!isReportingListenerRegistered) {
        notifier.addListener(reportingListener);
        isReportingListenerRegistered = true;
      }
    }
  }
}
