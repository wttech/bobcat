package com.cognifide.qa.bb.eyes;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.applitools.eyes.selenium.Eyes;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class WithEyes implements TestRule {

  private final WebDriver webDriver;
  private final Eyes eyes;
  private String appName;

  @Inject
  public WithEyes(WebDriver webDriver, Eyes eyes, @Named("eyes.appName") String appName) {
    this.webDriver = webDriver;
    this.eyes = eyes;
    this.appName = appName;
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }

  public Eyes getEyes() {
    return eyes;
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          WebDriver wrappedDriver = ((EventFiringWebDriver) webDriver).getWrappedDriver();
          eyes.open(wrappedDriver, appName, description.getMethodName());
          base.evaluate();
        } finally {
          closeEyes();
        }
      }
    };
  }

  private void closeEyes() {
    try {
      eyes.close();
    } finally {
      eyes.abortIfNotClosed();
    }
  }
}
