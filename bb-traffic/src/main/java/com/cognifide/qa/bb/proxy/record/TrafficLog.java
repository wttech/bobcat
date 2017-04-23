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
package com.cognifide.qa.bb.proxy.record;

import java.util.List;

import net.lightbody.bmp.core.har.Har;

/**
 * Container for captured network traffic
 */
public class TrafficLog {

  private final List<Har> hars;

  /**
   * Constructor. Initializes TrafficLog.
   *
   * @param hars list of captured Har objects
   */
  public TrafficLog(List<Har> hars) {
    super();
    this.hars = hars;
  }

  /**
   * @return list of captured Har objects
   */
  public List<Har> getHars() {
    return hars;
  }

  @Override
  public String toString() {
    return String.format("TrafficLog(contains %d HarEntries)", countItems());
  }

  private int countItems() {
    if (hars == null || hars.isEmpty()) {
      return 0;
    }
    return hars.stream().
            map(har -> har.getLog().getEntries().size()).
            reduce(0, Integer::sum);
  }

}
