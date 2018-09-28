/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.wait;

/**
 * Builder for {@link Timings} instances.
 * <p>
 * Default values are taken from System properties:
 * <ul>
 * <li>for explicit timeout - {@code wait.explicitTimeout}</li>
 * <li>for implicit timeout - {@code wait.implicitTimeout}</li>
 * <li>for polling time - {@code wait.pollingTime}</li>
 * </ul>
 */
public class TimingsBuilder {

  private long pollingTime = Long.valueOf(System.getProperty("wait.pollingTime", "500"));

  private long explicitTimeout = Long.valueOf(System.getProperty("wait.explicitTimeout", "10"));

  private long implicitTimeout = Long.valueOf(System.getProperty("wait.implicitTimeout", "10"));

  /**
   * Set the explicit timeout
   *
   * @param timeout timeout in seconds
   * @return a self reference
   */
  public TimingsBuilder explicitTimeout(long timeout) {
    explicitTimeout = timeout;
    return this;
  }

  /**
   * Set the polling time.
   *
   * @param time in milliseconds
   * @return a self reference
   */
  public TimingsBuilder pollingTime(long time) {
    pollingTime = time;
    return this;
  }

  /**
   * Set the implicit timeout.
   *
   * @param timeout in miliseconds
   * @return a self reference
   */
  public TimingsBuilder implicitTimeout(long timeout) {
    implicitTimeout = timeout;
    return this;
  }

  /**
   * Creates an instance of {@link Timings}
   *
   * @return new {@link Timings} instance
   */
  public Timings build() {
    return new Timings(explicitTimeout, pollingTime, implicitTimeout);
  }
}
