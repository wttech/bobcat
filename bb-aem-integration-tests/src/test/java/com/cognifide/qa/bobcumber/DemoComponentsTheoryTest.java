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
package com.cognifide.qa.bobcumber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DemoComponentsTheoryTest {

  @DataPoints
  public static Object[][] positiveIntegers() {
    return new Object[][] {
        {DemoComponents.RADIO_GROUP, "Radio_Group"},
        {DemoComponents.TEXT_FIELD_COMPONENT, "Text Field Component"},
        {DemoComponents.TITLE, "Title"},
        {DemoComponents.RADIO_GROUP, "radio group"},
        {DemoComponents.RADIO_GROUP, "Radio Group"},
        {DemoComponents.RADIO_GROUP, "RADIO_GROUP"},
        {DemoComponents.RADIO_GROUP, "RADIO_GROUP"},
        {DemoComponents.RADIO_GROUP, "RaDio_GrouP"},
        {DemoComponents.TEXT_FIELD_COMPONENT, "Text_Field_Component"}
    };
  }

  @Theory
  public void fromString_shouldReturnCorrectComponent(Object[] dataPoint) {

    DemoComponents expectedDemoComponent = (DemoComponents) dataPoint[0];
    String componentName = (String) dataPoint[1];

    DemoComponents actual = DemoComponents.fromString(componentName);
    assertThat(actual, is(expectedDemoComponent));
  }
}
