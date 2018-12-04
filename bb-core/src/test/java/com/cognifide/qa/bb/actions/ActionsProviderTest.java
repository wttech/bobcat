package com.cognifide.qa.bb.actions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

@ExtendWith(MockitoExtension.class)
class ActionsProviderTest {

  @Test
  void getReturnsActionsInstance() {
    assertDoesNotThrow(() -> Guice.createInjector(new TestModule()).getInstance(Actions.class));
  }

  class TestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(WebDriver.class).toInstance(mock(WebDriver.class));
      bind(Actions.class).toProvider(ActionsProvider.class);
    }
  }
}
