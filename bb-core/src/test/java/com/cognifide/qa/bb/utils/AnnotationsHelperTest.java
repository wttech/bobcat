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
package com.cognifide.qa.bb.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;

class AnnotationsHelperTest {

  @ParameterizedTest
  @CsvSource({
      "poAnnotated, true",
      "poiAnnotated, false",
      "listPoAnnotated, false",
      "listPoiAnnotated, false"
  })
  void shouldReturnTrueWhenClassIsAnnotatedWithPageObject(String field, boolean expected)
      throws NoSuchFieldException {
    assertThat(
        AnnotationsHelper.isPageObjectAnnotationPresent(TestData.class.getDeclaredField(field)))
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "poAnnotated, false",
      "poiAnnotated, true",
      "listPoAnnotated, false",
      "listPoiAnnotated, false"
  })
  void shouldReturnTrueWhenClassIsAnnotatedWithPageObjectInterface(String field, boolean expected)
      throws NoSuchFieldException {
    assertThat(AnnotationsHelper
        .isPageObjectInterfaceAnnotationPresent(TestData.class.getDeclaredField(field)))
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "poAnnotated, false",
      "poiAnnotated, false",
      "listPoAnnotated, true",
      "listPoiAnnotated, true"
  })
  void shouldReturnTrueWhenParameterizedClassIsAnnotatedWithPageObjectOrInterface(String field,
      boolean expected)
      throws NoSuchFieldException {
    assertThat(AnnotationsHelper
        .isGenericTypeAnnotatedWithPageObjectOrInterface(TestData.class.getDeclaredField(field)))
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "poAnnotated, false",
      "poiAnnotated, false",
      "listPoAnnotated, false",
      "listPoiAnnotated, false",
      "fpoPoAnnotated, true",
      "fpoPoiAnnotated, true",
      "fpoListPoAnnotated, true",
      "fpoListPoiAnnotated, true"
  })
  void shouldReturnTrueWhenAnnotatedWithFindPageObject(String field, boolean expected)
      throws NoSuchFieldException {
    assertThat(
        AnnotationsHelper.isFindPageObjectAnnotationPresent(TestData.class.getDeclaredField(field)))
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "poAnnotated, false",
      "poiAnnotated, false",
      "listPoAnnotated, false",
      "listPoiAnnotated, false",
      "fpoPoAnnotated, false",
      "fpoPoiAnnotated, false",
      "fpoListPoAnnotated, false",
      "fpoListPoiAnnotated, false",
      "fbPoAnnotated, true",
      "faPoAnnotated, true",
      "fbsPoAnnotated, true"
  })
  void shouldReturnTrueWhenAnnotatedByOneOfFindByAnnotations(String field, boolean expected)
      throws NoSuchFieldException {
    assertThat(AnnotationsHelper.isFindByAnnotationPresent(TestData.class.getDeclaredField(field)))
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "poAnnotated, false",
      "poiAnnotated, false",
      "listPoAnnotated, false",
      "listPoiAnnotated, false",
      "fpoPoAnnotated, false",
      "fpoPoiAnnotated, false",
      "fpoListPoAnnotated, false",
      "fpoListPoiAnnotated, false",
      "fbPoAnnotated, false",
      "faPoAnnotated, false",
      "fbsPoAnnotated, false",
      "globalPoAnnotated, true"
  })
  void shouldReturnTrueWhenGlobalAnnotationIsPresent(String field, boolean expected)
      throws NoSuchFieldException {
    assertThat(AnnotationsHelper.isGlobal(TestData.class.getDeclaredField(field)))
        .isEqualTo(expected);

  }

  private static class TestData {
    PoAnnotated poAnnotated;
    PoiAnnotated poiAnnotated;
    List<PoAnnotated> listPoAnnotated;
    List<PoiAnnotated> listPoiAnnotated;

    @FindPageObject
    PoAnnotated fpoPoAnnotated;
    @FindPageObject
    PoiAnnotated fpoPoiAnnotated;
    @FindPageObject
    List<PoAnnotated> fpoListPoAnnotated;
    @FindPageObject
    List<PoiAnnotated> fpoListPoiAnnotated;

    @FindBy
    PoAnnotated fbPoAnnotated;
    @FindAll(@FindBy)
    PoAnnotated faPoAnnotated;
    @FindBys(@FindBy)
    PoAnnotated fbsPoAnnotated;

    @Global
    PoAnnotated globalPoAnnotated;
  }


  @PageObject
  private static class PoAnnotated {
  }


  @PageObjectInterface
  private interface PoiAnnotated {

  }
}
