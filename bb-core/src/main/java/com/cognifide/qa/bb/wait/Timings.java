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
 * A value object defining the set of timings used in {@link BobcatWait}.
 * Use {@link TimingsBuilder} to create instances of this class.
 * <p>
 * Contains info about:
 * <ul>
 * <li>explicit timeout (in seconds)</li>
 * <li>implicit timeout (in seconds)</li>
 * <li>polling interval (in milliseconds)</li>
 * </ul>
 */
public class Timings {
  public static final int NEAR_ZERO = 1;
  public static final int DEFAULT_POLLING_INTERVAL = 500;
  public static final int DEFAULT_EXPLICIT_TIMEOUT = 10;
  public static final int DEFAULT_IMPLICIT_TIMEOUT = 1;
  private long explicitTimeout;
  private long pollingInterval;
  private long implicitTimeout;

  public Timings(long explicitTimeout, long pollingInterval, long implicitTimeout) {
    this.explicitTimeout = explicitTimeout;
    this.pollingInterval = pollingInterval;
    this.implicitTimeout = implicitTimeout;
  }

  public long getExplicitTimeout() {
    return explicitTimeout;
  }

  public long getPollingInterval() {
    return pollingInterval;
  }

  public long getImplicitTimeout() {
    return implicitTimeout;
  }
}
