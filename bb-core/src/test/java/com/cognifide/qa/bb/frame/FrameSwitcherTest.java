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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.when;

import java.util.Deque;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.Whitebox;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Provider;

public class FrameSwitcherTest {

  private static final String FRAME_PATH = "/frame/path";

  private static final String NAME = "name";

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();

  @InjectMocks
  public FrameSwitcher testedObject = new FrameSwitcher();

  @Mock
  private Provider<WebDriver> provider;

  @Mock
  private WebDriver webDriver;

  @Mock
  private BobcatWait bobcatWait;

  @Mock
  private FramePath framePath;

  @Before
  public void setUp() {
    when(provider.get()).thenReturn(webDriver);
  }

  @Test
  public void shouldHaveAtLeastOneElementInDequeOnConstruction() {
    //then
    assertThatDequeSizeIs(1);
  }

  @Test
  public void shouldSwitchBetweenFramesAndProvidedFrameShouldBeOnHead() {
    //when
    testedObject.switchTo(framePath);

    //then
    assertThatDequeSizeIs(2);
    assertThat(extractLocalDeque().peek()).isEqualTo(framePath);
  }

  @Test
  public void shouldSwitchBetweenFramesAndProvidedFramePathShouldBeOnHead() {
    //when
    testedObject.switchTo(FRAME_PATH);

    //then
    assertThatDequeSizeIs(2);
  }

  @Test
  public void shouldNotSwitchBackWhenThereAreNotEnoughFramesOnDeque() {
    //given
    assumeThatDequeSizeIs(1);

    //when
    testedObject.switchBack();

    //then
    assertThatDequeSizeIs(1);
  }

  @Test
  public void shouldSwitchBackWhenThereIsAtLeastTwoFramesOnDeque() {
    //given
    testedObject.switchTo(framePath);
    assumeThatDequeSizeIs(2);

    //when
    testedObject.switchBack();

    //then
    assertThatDequeSizeIs(1);
  }

  @Test
  public void shouldPutFrameOnStackById() {
    //given
    assumeThatDequeSizeIs(1);

    //when
    testedObject.putFramePathOnStack(1);

    //then
    assertThatDequeSizeIs(2);
  }

  @Test
  public void shouldPutFrameOnStackByName() {
    //given
    assumeThatDequeSizeIs(1);

    //when
    testedObject.putFramePathOnStack(NAME);

    //then
    assertThatDequeSizeIs(2);
  }

  private void assumeThatDequeSizeIs(int size) {
    assumeThat(extractLocalDeque().size(), is(size));
  }

  private void assertThatDequeSizeIs(int size) {
    assertThat(extractLocalDeque().size()).isEqualTo(size);
  }

  @SuppressWarnings("unchecked")
  private Deque<FramePath> extractLocalDeque() {
    return (Deque<FramePath>) Whitebox.getInternalState(testedObject, "localDeque");
  }
}
