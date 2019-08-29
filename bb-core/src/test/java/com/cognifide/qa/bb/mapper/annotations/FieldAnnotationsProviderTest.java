/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.mapper.annotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;

class FieldAnnotationsProviderTest {

  @ParameterizedTest
  @ValueSource(strings = {"findBy", "findBys", "findAll"})
  void shouldReturnSeleniumAnnotationsWhenFieldIsDecoratedWithFindBys(String field)
      throws NoSuchFieldException {
    assertThat(FieldAnnotationsProvider.create(TestData.class.getDeclaredField(field), null))
        .isInstanceOf(Annotations.class);
  }

  @Test
  void shouldReturnBobcatAnnotationsWhenFieldIsDecoratedWithFindPageObject()
      throws NoSuchFieldException {
    assertThat(
        FieldAnnotationsProvider.create(TestData.class.getDeclaredField("findPageObject"), null))
        .isInstanceOf(BobcatAnnotations.class);
  }

  @Test
  void shouldThrowWhenFieldIsNotDecoratedWithSupportedAnnotations() throws NoSuchFieldException {
    assertThatIllegalArgumentException().isThrownBy(() -> FieldAnnotationsProvider
        .create(TestData.class.getDeclaredField("notDecoratedWithSupported"), null));
  }

  private static class TestData {
    @FindBy
    TestData findBy;

    @FindBys(@FindBy)
    TestData findBys;

    @FindAll(@FindBy)
    TestData findAll;

    @FindPageObject
    TestData findPageObject;

    @Global
    TestData notDecoratedWithSupported;
  }
}
