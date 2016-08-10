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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.cognifide.qa.bb.scope.frame.type.AemContentFrame;
import com.cognifide.qa.bb.scope.frame.type.DefaultFrame;
import com.cognifide.qa.bb.scope.frame.type.IndexedFrame;
import com.cognifide.qa.bb.scope.frame.type.NamedFrame;

public class FramePathTest {

  @Test
  public void shouldEqualsBothEmptyInstances() throws Exception {
    // given
    FramePath cut = new FramePath();
    FramePath reference = new FramePath(new ArrayList<>());

    // then
    assertEquals(cut, reference);
    assertEquals(reference, cut);
  }

  @Test
  public void shouldNonEqualTwoDifferentInstances() throws Exception {
    // given
    FramePath cut = new FramePath();
    FramePath reference = new FramePath(Arrays.asList(spy(FrameDescriptor.class)));

    // then
    assertNotEquals(cut, reference);
    assertNotEquals(reference, cut);
  }

  @Test
  public void shouldHaveAnEmptyFramesCollectionWhenEmptyPathIsProvided() throws Exception {
    // when
    FramePath cut = FramePath.parsePath("");

    // then
    assertTrue(cut.getFrames().isEmpty());
  }

  @Test
  public void shouldHaveOneNamedFrameCollectionWhenOnlyOnePathElementProvided() throws Exception {
    // given
    String frameName = "artificial-named-frame";

    // when
    FramePath cut = FramePath.parsePath(frameName);

    // then
    assertThat(cut.getFrames().size(), is(1));
    assertThat(cut.getFrames(), hasItem(new NamedFrame(frameName)));
  }

  @Test
  public void shouldHaveOneIndexedFrameWhenOneNumberPathElementProvided() throws Exception {
    // given
    int frameIndex = 123;

    // when
    FramePath cut = FramePath.parsePath("/$" + frameIndex);

    // then
    assertThat(cut.getFrames().size(), is(1));
    assertThat(cut.getFrames(), hasItem(new IndexedFrame(frameIndex)));
  }

  @Test
  public void shouldHaveOneAemContentFrameWhenOneSpecialStringProvided() throws Exception {
    // when
    FramePath cut = FramePath.parsePath("$cq");

    // then
    assertThat(cut.getFrames().size(), is(1));
    assertThat(cut.getFrames(), hasItem(AemContentFrame.INSTANCE));
  }

  @Test
  public void shouldRemoveTheMiddleNamedFrameIfHasDoubleDot() throws Exception {
    // given
    String firstFrameName = "first-named-frame";
    String secondFrameName = "second-named-frame";
    String thirdFrameName = "third-named-frame";

    // when
    FramePath cut =
        FramePath.parsePath(firstFrameName + "/" + secondFrameName + "/../" + thirdFrameName);

    // then
    assertThat(cut.getFrames().size(), is(2));
    assertThat(cut.getFrames(), hasItem(new NamedFrame(firstFrameName)));
    assertThat(cut.getFrames(), hasItem(new NamedFrame(thirdFrameName)));
    assertThat(cut.getFrames(), not(hasItem(new NamedFrame(secondFrameName))));
  }

  @Test
  public void shouldGetADiffWithEmptyFramePath() throws Exception {
    // given
    String firstFrameName = "first-named";
    String secondFrameName = "second-named";
    FramePath cut = FramePath.parsePath("/" + firstFrameName + "/" + secondFrameName);
    FramePath emptyFramePath = FramePath.parsePath("/");

    // when
    List<FrameDescriptor> result = emptyFramePath.diff(cut);

    // then
    assertThat(result.size(), is(2));
    assertThat(result, hasItem(new NamedFrame(firstFrameName)));
    assertThat(result, hasItem(new NamedFrame(secondFrameName)));
  }

  @Test
  public void shouldGetADiffWithSubpathFrame() throws Exception {
    // given
    String firstFrameName = "first-named";
    String secondFrameName = "second-named";
    FramePath cut = FramePath.parsePath("/" + firstFrameName + "/" + secondFrameName);
    FramePath emptyFramePath = FramePath.parsePath("/" + firstFrameName);

    // when
    List<FrameDescriptor> result = emptyFramePath.diff(cut);

    // then
    assertThat(result.size(), is(1));
    assertThat(result, hasItem(new NamedFrame(secondFrameName)));
  }

  @Test
  public void shouldGetAFullDiffWithDifferentPaths() throws Exception {
    // given
    String firstFrameName = "first-named";
    String secondFrameName = "second-named";
    FramePath cut = FramePath.parsePath("/" + firstFrameName + "/" + secondFrameName);
    FramePath emptyFramePath = FramePath.parsePath("/" + secondFrameName + "/" + firstFrameName);

    // when
    List<FrameDescriptor> result = emptyFramePath.diff(cut);

    // then
    assertThat(result.size(), is(3));
    assertThat(result, hasItem(DefaultFrame.INSTANCE));
    assertThat(result, hasItem(new NamedFrame(secondFrameName)));
    assertThat(result, hasItem(new NamedFrame(firstFrameName)));
  }
}
