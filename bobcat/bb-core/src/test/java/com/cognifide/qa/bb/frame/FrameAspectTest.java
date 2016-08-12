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
package com.cognifide.qa.bb.frame;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Provider;

public class FrameAspectTest {

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();

  @InjectMocks
  public MethodInterceptor testedObject = new FrameAspect();

  @Mock
  private FrameMap frameMap;

  @Mock
  private Provider<FrameSwitcher> provider;

  @Mock
  private FrameSwitcher frameSwitcher;

  @Mock
  private MethodInvocation methodInvocation;

  @Mock
  private FramePath framePath;

  @Before
  public void setUp() {
    when(provider.get()).thenReturn(frameSwitcher);

  }

  @Test
  public void shouldInvokeMethodWithoutChangingFrameWhenMethodComesFromObject() throws Throwable {
    //given
    Method method = Object.class.getDeclaredMethod("getClass");
    when(methodInvocation.getMethod()).thenReturn(method);

    //when
    Object actual = testedObject.invoke(methodInvocation);

    //then
    assertThat(actual).isNull();

    verifyNoMoreInteractions(frameMap, provider, frameSwitcher);
  }

  @Test
  public void shouldInvokeMethodWithFrameChangeWhenMethodComesFromClass() throws Throwable {
    //given
    Element element = new Element();
    setUpMethodInvocationWith(Element.class, element);

    //when
    Object actual = testedObject.invoke(methodInvocation);

    //then
    assertThat(actual).isNull();

    verify(provider).get();
    verify(methodInvocation, times(2)).getMethod();
    verify(methodInvocation).getThis();
    verify(frameMap).get(element);
    verify(frameSwitcher).switchTo(framePath);
    verify(methodInvocation).proceed();
    verify(frameSwitcher).switchBack();
    verifyNoMoreInteractions(provider, frameMap, frameSwitcher, methodInvocation);
  }

  @Test
  public void shouldInvokeMethodWithFrameChangeWhenMethodIsAnnotatedWithFrame() throws Throwable {
    //given
    AnnotatedElement element = new AnnotatedElement();
    setUpMethodInvocationWith(AnnotatedElement.class, element);
    when(framePath.addFrame(any(Frame.class))).thenReturn(framePath);

    //when
    Object actual = testedObject.invoke(methodInvocation);

    //then
    assertThat(actual).isNull();

    verify(provider).get();
    verify(methodInvocation, times(2)).getMethod();
    verify(methodInvocation).getThis();
    verify(frameMap).get(element);
    verify(framePath).addFrame(any(Frame.class));
    verify(frameSwitcher).switchTo(framePath);
    verify(methodInvocation).proceed();
    verify(frameSwitcher).switchBack();
    verifyNoMoreInteractions(provider, frameMap, frameSwitcher, methodInvocation);
  }

  private void setUpMethodInvocationWith(Class<?> type, Object element)
      throws NoSuchMethodException {
    Method method = type.getDeclaredMethod("execute");
    when(methodInvocation.getMethod()).thenReturn(method);

    when(methodInvocation.getThis()).thenReturn(element);
    when(frameMap.get(element)).thenReturn(framePath);
  }

  private class Element {
    void execute() {}
  }

  private class AnnotatedElement {
    @Frame(value = "")
    void execute() {}
  }

}
