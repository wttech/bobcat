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
package com.cognifide.qa.bb.logging.entries;

/**
 * This class represents a webDriver event.
 */
public class EventEntry extends LogEntry {

  private final String event;

  private final String parameter;

  private final long duration;

  /**
   * Constructs an EventEntry instance.
   *
   * @param event     name of an event
   * @param parameter event parameter
   * @param duration  how long the event took (in milliseconds)
   */
  public EventEntry(String event, String parameter, long duration) {
    super();
    this.event = event;
    this.parameter = parameter;
    this.duration = duration;
  }

  /**
   * @return Event description.
   */
  public String getEvent() {
    return event;
  }

  /**
   * @return Event parameters.
   */
  public String getParameter() {
    return parameter;
  }

  /**
   * @return Duration of the event.
   */
  public long getDuration() {
    return duration;
  }
}
