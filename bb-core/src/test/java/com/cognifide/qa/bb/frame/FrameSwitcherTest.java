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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Provider;

@ExtendWith(MockitoExtension.class)
public class FrameSwitcherTest {

  private static final String FRAME_PATH = "/frame/path";

  private static final String NAME = "name";

  @InjectMocks
  public FrameSwitcher testedObject = new FrameSwitcher();

  @Mock
  private Provider<WebDriver> provider;

  @Mock
  private BobcatWait bobcatWait;

  @Mock
  private FramePath framePath;

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
    assertThat(testedObject.getLocalDeque().peek()).isEqualTo(framePath);
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
    assumeTrue(testedObject.getLocalDeque().size() == size);
  }

  private void assertThatDequeSizeIs(int size) {
    assertThat(testedObject.getLocalDeque().size()).isEqualTo(size);
  }
}
