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
