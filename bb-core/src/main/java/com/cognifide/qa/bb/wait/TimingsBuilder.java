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

import com.cognifide.qa.bb.constants.ConfigKeys;

/**
 * Builder for {@link Timings} instances.
 * <p>
 * Default values are taken from System properties:
 * <ul>
 * <li>for explicit timeout - {@value ConfigKeys#TIMINGS_EXPLICIT_TIMEOUT}</li>
 * <li>for implicit timeout - {@value ConfigKeys#TIMINGS_IMPLICIT_TIMEOUT}</li>
 * <li>for polling interval - {@value ConfigKeys#TIMINGS_POLLING_INTERVAL}</li>
 * </ul>
 */
public class TimingsBuilder {

  private long pollingInterval =
      Long.valueOf(System.getProperty(ConfigKeys.TIMINGS_POLLING_INTERVAL,
          String.valueOf(Timings.DEFAULT_POLLING_INTERVAL)));

  private long explicitTimeout =
      Long.valueOf(System.getProperty(ConfigKeys.TIMINGS_EXPLICIT_TIMEOUT,
          String.valueOf(Timings.DEFAULT_EXPLICIT_TIMEOUT)));

  private long implicitTimeout =
      Long.valueOf(System.getProperty(ConfigKeys.TIMINGS_IMPLICIT_TIMEOUT,
          String.valueOf(Timings.DEFAULT_IMPLICIT_TIMEOUT)));

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
  public TimingsBuilder pollingInterval(long time) {
    pollingInterval = time;
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
    return new Timings(explicitTimeout, pollingInterval, implicitTimeout);
  }
}
