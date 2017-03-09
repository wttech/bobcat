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
package com.cognifide.qa.bb.provider.jcr.properties;

/**
 * Interface for properties of instances.
 * Author and publish environments has unique implementation.
 */
public interface InstanceProperties {

  /**
   * @return IP address of desired environment
   */
  String getIp();

  /**
   * @return login for desired environment
   */
  String getLogin();

  /**
   * @return password for desired environment
   */
  String getPassword();

}
