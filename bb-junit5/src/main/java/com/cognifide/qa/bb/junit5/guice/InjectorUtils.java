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
import java.lang.reflect.AnnotatedElement;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

/**
 * Extensions helper class for retriving injector created in {@link GuiceExtension}
 */
public final class InjectorUtils {

  private InjectorUtils() {
    //for util class
  }

  /**
   * Retrieves injector from the context store using Namespace. If it is not present it searches in
   * parent context until it is found or the is end of hierachy
   *
   * @param context - extension context from  junit5 extensions
   * @param namespace - Namespace for current test invocation
   * @return Injector or null if no injector is found
   */
  public static Injector retrieveInjectorFromStore(ExtensionContext context, Namespace namespace) {
    AnnotatedElement element = context.getElement()
        .orElseThrow(() -> new NoSuchElementException("No element present"));

    return context.getStore(namespace).getOrComputeIfAbsent(element,
        absent -> retrieveInjectorFromStore(
            context.getParent().orElseThrow(() -> new NoSuchElementException("No injector found")),
            namespace), Injector.class);
  }
}
