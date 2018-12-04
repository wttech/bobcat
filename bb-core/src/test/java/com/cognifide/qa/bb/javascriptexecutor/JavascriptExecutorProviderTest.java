package com.cognifide.qa.bb.javascriptexecutor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

@ExtendWith(MockitoExtension.class)
class JavascriptExecutorProviderTest {

  @Test
  void getReturnsJavasriptExecutorInstance() {
    assertDoesNotThrow(() -> Guice
        .createInjector(new TestModule()).getInstance(JavascriptExecutor.class));
  }

  class TestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(WebDriver.class).toInstance(
          mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class)));
      bind(JavascriptExecutor.class).toProvider(JavascriptExecutorProvider.class);
    }
  }
}
