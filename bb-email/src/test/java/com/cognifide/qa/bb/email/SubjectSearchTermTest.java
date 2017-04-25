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
package com.cognifide.qa.bb.email;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import javax.mail.Message;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(Parameterized.class)
public class SubjectSearchTermTest {

  public static final String VALID_SUBJECT = "valid";

  public static final String INVALID_SUBJECT = "invalid";

  private final boolean expected;

  private final String subject;

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private Message message;

  public SubjectSearchTermTest(String subject, boolean expected) {
    this.expected = expected;
    this.subject = subject;
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        {VALID_SUBJECT, true},
        {INVALID_SUBJECT, false},
        {StringUtils.EMPTY, false},
        {null, false}
    });
  }

  @Test
  public void match() throws Exception {

    when(message.getSubject()).thenReturn(VALID_SUBJECT);

    boolean actual = new SubjectSearchTerm(subject).match(message);

    assertEquals(expected, actual);
  }

}