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
package com.cognifide.qa.bb.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation can be added to any page object class or any method in page object. It causes a method (or
 * all methods in given class) to be executed inside frame specified as the annotation value. The value is a
 * path, eg.:
 * </p>
 * <pre>
 * /my-frame/subframe2/$3
 * </pre>
 * <p>
 * "my-frame" and "subframe2" parts are frame names. $3 is a frame index (it starts with $0). If the path
 * starts with /, it's considered as a "absolute" path, starting from the default window content. Otherwise
 * it's considered as path relative to the parent page object. It is also possible to use ".." to switch to a
 * parent frame. Examples:
 * </p>
 * <ul>
 * <li>/ - default content</li>
 * <li>../.. - grandparent frame</li>
 * <li>frame1 - relative subframe with appropriate name</li>
 * <li>/$0 - first subframe of the default content</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface Frame {
  String value();
}
