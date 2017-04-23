/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.utils;

public final class AopUtil {

  private AopUtil() {
    //util class
  }

  /**
   * @param object object
   * @return Non-proxied class name of provided object
   */
  public static Class<?> getBaseClassForAopObject(Object object) {
    if (object instanceof Class) {
      return getBaseClassForAopObject((Class) object);
    }
    Class<?> clazz = object.getClass();
    return getBaseClassForAopObject(clazz);
  }

  private static Class<?> getBaseClassForAopObject(Class clazz) {
    if (clazz.getName().contains("EnhancerByGuice")) {
      return clazz.getSuperclass();
    }
    return clazz;
  }
}
