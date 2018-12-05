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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.AnnotatedElement;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.inject.Injector;

@ExtendWith(MockitoExtension.class)
class InjectorUtilsTest {

  @Mock
  private ExtensionContext context;

  @Mock
  private AnnotatedElement annotatedElement;

  @Mock
  private ExtensionContext.Store store;

  @Mock
  private Injector injector;

  private ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create("test");

  @Test
  void shouldRetrieveInjectorWhenPresent() {
    Optional<AnnotatedElement> optional = Optional.of(annotatedElement);
    when(context.getElement()).thenReturn(optional);
    when(store.getOrComputeIfAbsent(any(), any(), eq(Injector.class))).thenReturn(injector);
    when(context.getStore(namespace)).thenReturn(store);

    assertEquals(injector, InjectorUtils.retrieveInjectorFromStore(context, namespace));
  }

  @Test
  void shouldThrowWhenInjectorIsMissing() {
    Optional<AnnotatedElement> optional = Optional.of(annotatedElement);
    when(context.getElement()).thenReturn(optional);
    when(store.getOrComputeIfAbsent(any(), any(), eq(Injector.class)))
        .thenThrow(NoSuchElementException.class);
    when(context.getStore(namespace)).thenReturn(store);

    assertThrows(NoSuchElementException.class,
        () -> InjectorUtils.retrieveInjectorFromStore(context, namespace));
  }
}
