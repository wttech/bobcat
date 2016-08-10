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
package com.cognifide.qa.bb.mapper.field;

import java.lang.reflect.Field;
import java.util.Optional;

import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;

/**
 * This is a provider class that is able to determine the frame that we're currently in. It should not be used
 * directly by Bobcat's users. Instead, client classes will include a field with annotations Inject and
 * CurrentFrame. Bobcat will inject such field with the current frame, as determined by CurrentFrameProvider.
 */
public class CurrentFrameProvider implements FieldProvider {

  @Inject
  private FrameMap frameMap;

  /**
   * PageObjectInjectorListener calls this method to check if the provider is able to handle currently
   * injected field.
   * <p>
   * CurrentFrameProvider handles fields decorated with CurrentFrame annotation.
   */
  @Override
  public boolean accepts(Field field) {
    return field.isAnnotationPresent(CurrentFrame.class);
  }

  /**
   * This method returns the current frame path with the type aligned with the injected field.
   */
  @Override
  public Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context) {
    final Class<?> type = field.getType();
    FramePath path = frameMap.get(pageObject);

    if (path == null) {
      path = new FramePath();
    }

    Object currentFrame = null;
    if (String.class.equals(type)) {
      currentFrame = path.toString();
    } else if (FramePath.class.isAssignableFrom(type)) {
      currentFrame = path;
    }

    return Optional.ofNullable(currentFrame);
  }
}
