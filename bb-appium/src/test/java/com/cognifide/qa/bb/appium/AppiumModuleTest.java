package com.cognifide.qa.bb.appium;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.cognifide.qa.bb.appium.modules.AppiumModule;
import com.google.inject.Guice;
import com.google.inject.Stage;

class AppiumModuleTest {

  @Test
  void allBindingsAreConfiguredCorrectly() {
    assertDoesNotThrow(() -> Guice.createInjector(Stage.TOOL, new AppiumModule()));
  }
}
