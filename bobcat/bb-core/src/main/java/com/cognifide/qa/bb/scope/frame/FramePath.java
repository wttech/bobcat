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



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.scope.frame.type.AemContentFrame;
import com.cognifide.qa.bb.scope.frame.type.DefaultFrame;
import com.cognifide.qa.bb.scope.frame.type.IndexedFrame;
import com.cognifide.qa.bb.scope.frame.type.NamedFrame;

/**
 * This class represents an address of the frame in the hierarchy of frames within a page. The representation has form
 * of list of FrameDescriptor objects that represent individual frames.
 */
public class FramePath {

  private final List<FrameDescriptor> frames;

  /**
   * Constructs FramePath.
   * <p>
   * Initializes list of frames to an empty list.
   */
  public FramePath() {
    this.frames = Collections.emptyList();
  }

  /**
   * Constructs FramePath.
   *
   * @param frames Constructor will initialize FramePath's list of frames with this parameter.
   */
  public FramePath(List<FrameDescriptor> frames) {
    this.frames = new ArrayList<>(frames);
  }

  /**
   * Constructs FramePath.
   *
   * @param framePath Constructor will initialize FramePaths's list of frames with frames from frame path
   * @param frames    Constructor will add listed frames to FramePath's list of frames
   */
  public FramePath(FramePath framePath, FrameDescriptor... frames) {
    this.frames = new ArrayList<>(framePath.getFrames());
    this.frames.addAll(Arrays.asList(frames));
  }

  /**
   * Factory method that parses a string into a FramePath object.
   *
   * @param path String to be parsed to FramePath object.
   * @return FramePath instance.
   */
  public static FramePath parsePath(String path) {
    return new FramePath().addFrame(path);
  }

  /**
   * Appends the frame to the frame path.
   *
   * @param frame Frame to be attached to the frame path.
   * @return FramePath with the appended frame.
   */
  public FramePath addFrame(Frame frame) {
    return addFrame(frame.value());
  }

  /**
   * @return Frame path as a list of FrameDescriptors.
   */
  public List<FrameDescriptor> getFrames() {
    return Collections.unmodifiableList(frames);
  }

  /**
   * This method calculates diff between frame paths.
   *
   * @param to FramePath that will be taken to diff calculation.
   * @return difference between FramePath's
   */
  public List<FrameDescriptor> diff(FramePath to) {
    List<FrameDescriptor> toFrames = to.getFrames();
    List<FrameDescriptor> diff = new ArrayList<>();

    if (isSubpath(to)) {
      for (int i = frames.size(); i < toFrames.size(); i++) {
        diff.add(toFrames.get(i));
      }
    } else {
      diff.add(DefaultFrame.INSTANCE);
      diff.addAll(toFrames);
    }
    return diff;
  }

  @Override
  public int hashCode() {
    return Objects.hash(frames);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }

    return Objects.equals(frames, ((FramePath) obj).frames);
  }

  @Override
  public String toString() {
    return StringUtils.join(frames, '/').replace("//", "/");
  }

  private FramePath addFrame(String path) {
    if (StringUtils.isEmpty(path)) {
      return this;
    }
    final String firstPart = StringUtils.substringBefore(path, "/");
    final String rest = StringUtils.substringAfter(path, "/");

    List<FrameDescriptor> modifiedLocation = new ArrayList<>(frames);
    if (firstPart.isEmpty()) {
      modifiedLocation.clear();
    } else if (firstPart.matches("\\$[0-9]+")) {
      final int index = Integer.parseInt(firstPart.substring(1));
      modifiedLocation.add(new IndexedFrame(index));
    } else if ("$cq".equals(firstPart)) {
      modifiedLocation.clear();
      modifiedLocation.add(AemContentFrame.INSTANCE);
    } else if ("..".equals(firstPart) && !modifiedLocation.isEmpty()) {
      modifiedLocation.remove(modifiedLocation.size() - 1);
    } else {
      modifiedLocation.add(new NamedFrame(firstPart));
    }
    return new FramePath(modifiedLocation).addFrame(rest);
  }

  private boolean isSubpath(FramePath subpathCandidate) {
    final List<FrameDescriptor> toFrames = subpathCandidate.getFrames();
    boolean subpath = true;
    if (frames.size() <= toFrames.size()) {
      for (int i = 0; i < frames.size(); i++) {
        if (!frames.get(i).equals(toFrames.get(i))) {
          subpath = false;
          break;
        }
      }
    } else {
      subpath = false;
    }
    return subpath;
  }
}
