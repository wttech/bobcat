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
package com.cognifide.bdd.demo.po.feedback;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.constants.HtmlTags;

@RunWith(Theories.class)
public class RichtextComponentTest {

  @DataPoints
  public static Object[][] prepareDataForTests() {
    return new Object[][] {
        {"container-fluid", "container-fluid", true},
        {"header clearfix", "clearfix", true},
        {"header clearfix ", "clearfix", true},
        {"header clearfix top", "clearfix", true},
        {"header clearfix top", "", true},
        {"header clearfix top", null, true},
        {"", "", true},
        {"", null, true},

        {"header clearfix top", "footer", false},
        {"header clearfix top", "header clearfix", false},
        {"header clearfix ", "clear", false},
        {null, "footer", false},
        {null, "", false},
        {null, null, false}
    };
  }

  @Theory
  public void shouldProperlyRecognizeCssClassInWebElement(Object[] dataPoint) throws Exception {
    RichtextComponent testedObject = new RichtextComponent();
    String classAttributeValue = (String) dataPoint[0];
    String checkedClass = (String) dataPoint[1];
    boolean expectedResult = (boolean) dataPoint[2];

    WebElement mockWebElement = mock(WebElement.class);
    when(mockWebElement.getAttribute(eq(HtmlTags.Attributes.CLASS)))
        .thenReturn(classAttributeValue);

    boolean actual = testedObject.checkIfHasClass(mockWebElement, checkedClass);

    String msg = String
        .format("Should return '%s' for class attribute: '%s' and checked class: '%s'",
            expectedResult, classAttributeValue, checkedClass);
    assertThat(msg, actual, is(expectedResult));
  }

}
