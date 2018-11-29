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
package com.cognifide.qa.bb.scope.frame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cognifide.qa.bb.scope.frame.type.AemContentFrame;
import com.cognifide.qa.bb.scope.frame.type.DefaultFrame;
import com.cognifide.qa.bb.scope.frame.type.IndexedFrame;
import com.cognifide.qa.bb.scope.frame.type.NamedFrame;

public class FramePathTest {

  @Test
  public void shouldEqualsBothEmptyInstances() {
    // given
    FramePath cut = new FramePath();
    FramePath reference = new FramePath(new ArrayList<>());

    // then
    assertEquals(cut, reference);
    assertEquals(reference, cut);
  }

  @Test
  public void shouldNonEqualTwoDifferentInstances() {
    // given
    FramePath cut = new FramePath();
    FramePath reference = new FramePath(Collections.singletonList(spy(FrameDescriptor.class)));

    // then
    assertNotEquals(cut, reference);
    assertNotEquals(reference, cut);
  }

  @Test
  public void shouldHaveAnEmptyFramesCollectionWhenEmptyPathIsProvided() {
    // when
    FramePath cut = FramePath.parsePath("");

    // then
    assertTrue(cut.getFrames().isEmpty());
  }

  @Test
  public void shouldHaveOneNamedFrameCollectionWhenOnlyOnePathElementProvided() {
    // given
    String frameName = "artificial-named-frame";

    // when
    FramePath cut = FramePath.parsePath(frameName);

    // then
    assertThat(cut.getFrames().size()).isEqualTo(1);
    assertThat(cut.getFrames()).contains(new NamedFrame(frameName));
  }

  @Test
  public void shouldHaveOneIndexedFrameWhenOneNumberPathElementProvided() {
    // given
    int frameIndex = 123;

    // when
    FramePath cut = FramePath.parsePath("/$" + frameIndex);

    // then
    assertThat(cut.getFrames().size()).isEqualTo(1);
    assertThat(cut.getFrames()).contains(new IndexedFrame(frameIndex));
  }

  @Test
  public void shouldHaveOneAemContentFrameWhenOneSpecialStringProvided() {
    // when
    FramePath cut = FramePath.parsePath("$cq");

    // then
    assertThat(cut.getFrames().size()).isEqualTo(1);
    assertThat(cut.getFrames()).contains(AemContentFrame.INSTANCE);
  }

  @Test
  public void shouldRemoveTheMiddleNamedFrameIfHasDoubleDot() {
    // given
    String firstFrameName = "first-named-frame";
    String secondFrameName = "second-named-frame";
    String thirdFrameName = "third-named-frame";

    // when
    FramePath cut =
        FramePath.parsePath(firstFrameName + "/" + secondFrameName + "/../" + thirdFrameName);

    // then
    assertThat(cut.getFrames().size()).isEqualTo(2);
    assertThat(cut.getFrames()).contains(new NamedFrame(firstFrameName));
    assertThat(cut.getFrames()).contains(new NamedFrame(thirdFrameName));
    assertThat(cut.getFrames()).doesNotContain(new NamedFrame(secondFrameName));
  }

  @Test
  public void shouldGetADiffWithEmptyFramePath() {
    // given
    String firstFrameName = "first-named";
    String secondFrameName = "second-named";
    FramePath cut = FramePath.parsePath("/" + firstFrameName + "/" + secondFrameName);
    FramePath emptyFramePath = FramePath.parsePath("/");

    // when
    List<FrameDescriptor> result = emptyFramePath.diff(cut);

    // then
    assertThat(result.size()).isEqualTo(2);
    assertThat(result).contains(new NamedFrame(firstFrameName));
    assertThat(result).contains(new NamedFrame(secondFrameName));
  }

  @Test
  public void shouldGetADiffWithSubpathFrame() {
    // given
    String firstFrameName = "first-named";
    String secondFrameName = "second-named";
    FramePath cut = FramePath.parsePath("/" + firstFrameName + "/" + secondFrameName);
    FramePath emptyFramePath = FramePath.parsePath("/" + firstFrameName);

    // when
    List<FrameDescriptor> result = emptyFramePath.diff(cut);

    // then
    assertThat(result.size()).isEqualTo(1);
    assertThat(result).contains(new NamedFrame(secondFrameName));
  }

  @Test
  public void shouldGetAFullDiffWithDifferentPaths() {
    // given
    String firstFrameName = "first-named";
    String secondFrameName = "second-named";
    FramePath cut = FramePath.parsePath("/" + firstFrameName + "/" + secondFrameName);
    FramePath emptyFramePath = FramePath.parsePath("/" + secondFrameName + "/" + firstFrameName);

    // when
    List<FrameDescriptor> result = emptyFramePath.diff(cut);

    // then
    assertThat(result.size()).isEqualTo(3);
    assertThat(result).contains(DefaultFrame.INSTANCE);
    assertThat(result).contains(new NamedFrame(secondFrameName));
    assertThat(result).contains(new NamedFrame(firstFrameName));
  }
}
