/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.junit5.guice;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.reflect.AnnotatedElement;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.openqa.selenium.WebDriver;

public class WebdriverCloseExtension implements AfterEachCallback {


  private static final Namespace NAMESPACE =
      Namespace.create("com", "cognifide", "qa", "bb", "junit", "guice");

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (!context.getElement().isPresent()) {
      return;
    }

    AnnotatedElement element = context.getElement().get();
    Store store = context.getStore(NAMESPACE);

    Injector injector = store.get(element, Injector.class);
    if (injector != null) {
      injector.getInstance(Key.get(WebDriver.class)).quit();
    }
  }
}
