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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.WebDriverClosedListener;
import com.cognifide.qa.bb.scope.frame.FrameDescriptor;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.scope.frame.type.IndexedFrame;
import com.cognifide.qa.bb.scope.frame.type.NamedFrame;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * <p>
 * This class is the core of Bobcat's frame switching feature. It stores information about current frame and all
 * previously visited frames.
 * </p>
 * <p>
 * FrameSwitcher is thread-local, which means that each thread will use its own instance of this class.
 * </p>
 * <p>
 * FrameSwitcher is a WebDriverClosedListener. It resets the frame stack automatically when the webdriver is closed.
 * </p>
 */
@ThreadScoped
public class FrameSwitcher implements WebDriverClosedListener {

  private final Deque<FramePath> localDeque = new ArrayDeque<>();

  @Inject
  private Provider<WebDriver> provider;

  @Inject
  private BobcatWait bobcatWait;

  private boolean switchRequestComingFromFrameSwitcher;

  /**
   * Default constructor. It should not be used directly by the user. FrameSwitcher instances will be created by
   * Guice.
   */
  public FrameSwitcher() {
    localDeque.push(new FramePath());
  }

  /**
   * <p>
   * Use this method to manually switch to the frame you need.
   * </p>
   * <p>
   * The target frame will be put on the frame stack, so that all subsequent frame-aware calls will be performed
   * within it.
   * </p>
   * <p>
   * Remember to call {@link #switchBack() switchBack} when you no longer need to operate within your manually defined
   * frame.
   * </p>
   *
   * @param framePath FrameSwitcher will switch to the frame defined by this FramePath.
   */
  public void switchTo(FramePath framePath) {
    doSwitch(localDeque.peek(), framePath);
    localDeque.push(framePath);
  }

  /**
   * <p>
   * Takes the top frame off the stack and switches to the previous frame.
   * </p>
   * <p>
   * Currently Bobcat doesn't check that there is any frame to switch back to or that you have manually switched
   * before this method was called. Double check your manual switching to avoid weird side effects.
   * </p>
   */
  public void switchBack() {
    if (localDeque.size() > 1) {
      doSwitch(localDeque.poll(), localDeque.peek());
    }
  }

  /**
   * <p>
   * Use this method to manually switch to the frame you need.
   * </p>
   * <p>
   * This frame will be put on the stack, so all subsequent frame-aware calls will be performed within it.
   * </p>
   * <p>
   * Remember to call {@link #switchBack() switchBack} when you no longer need to operate within your manually defined
   * frame.
   * </p>
   *
   * @param path Frame path provided as String. Bobcat will first parse it into FramePath object before it performs
   *             switching.
   */
  public void switchTo(String path) {
    if (!getCurrentFramePath().equals(path)) {
      switchTo(FramePath.parsePath(path));
    }
  }

  /**
   * This method is automatically called by the webDriver. It should not be called manually by the user. It resets the
   * stack frame when the web driver is closed.
   */
  @Override
  public void onWebDriverClosed(boolean terminated) {
    reset();
  }

  /**
   * Puts the IndexedFrame descriptor on the frame stack, without performing switching. TargetLocator uses this method
   * when Bobcat user does switching using webDriver directly. Don't call this method manually.
   *
   * @param index of the frame
   */
  public void putFramePathOnStack(int index) {
    if (!switchRequestComingFromFrameSwitcher) {
      expandFramePathStack(new IndexedFrame(index));
    }
  }

  /**
   * Puts the NamedFrame descriptor on the frame stack, without performing switching. TargetLocator uses this method
   * when Bobcat user does switching using webDriver directly. Don't call this method manually.
   *
   * @param nameOrId name of the frame
   */
  public void putFramePathOnStack(String nameOrId) {
    if (!switchRequestComingFromFrameSwitcher) {
      expandFramePathStack(new NamedFrame(nameOrId));
    }
  }

  /**
   * Puts the DefaultFrame descriptor on the frame stack, without performing switching. TargetLocator uses this method
   * when Bobcat user does switching using webDriver directly. Don't call this method manually.
   */
  public void putDefaultFramePathOnStack() {
    if (!switchRequestComingFromFrameSwitcher) {
      localDeque.push(new FramePath());
    }
  }

  /**
   * Resetting the stack frame means clearing it and putting there the default frame
   */
  public void reset() {
    localDeque.clear();
    localDeque.push(new FramePath());
  }

  /**
   *
   * @return path of the current frame
   */
  public String getCurrentFramePath() {
    return localDeque.getFirst().toString();
  }

  private void doSwitch(FramePath current, FramePath destination) {
    final List<FrameDescriptor> diff = current.diff(destination);
    final WebDriver driver = provider.get();

    switchRequestComingFromFrameSwitcher = true;
    diff.forEach(frameDescriptor -> frameDescriptor.switchTo(driver, bobcatWait));
    switchRequestComingFromFrameSwitcher = false;
  }

  private void expandFramePathStack(FrameDescriptor additionalFrame) {
    localDeque.push(new FramePath(localDeque.peek(), additionalFrame));
  }
}
