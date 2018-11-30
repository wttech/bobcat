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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provider for instances of {@link JavascriptExecutor}.
 * <p>
 * Users are encouraged to inject instances directly:
 * <p>
 * {@code
 *
 * @Inject private JavascriptExecutor jsExecutor;}
 */
public class JavascriptExecutorProvider implements Provider<JavascriptExecutor> {

  @Inject
  private WebDriver webDriver;

  @Override
  public JavascriptExecutor get() {
    return ((JavascriptExecutor) webDriver);
  }
}
