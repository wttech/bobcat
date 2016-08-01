package com.cognifide.qa.bb.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

/**
 * Helper class with methods for managing annotations.
 */
public class AnnotationsHelper {

  private static final Class<?>[] FIND_ANNOTATIONS =
      new Class<?>[] {FindAll.class, FindBy.class, FindBys.class};

  private AnnotationsHelper() {
    // class with only static methods
  }

  /**
   * Checks if field contains {@link FindPageObject} annotation
   * @param field field to check
   * @return if annotation is present
   */
  public static boolean isFindPageObjectAnnotationPresent(Field field) {
    return field.isAnnotationPresent(FindPageObject.class);
  }

  /**
   *  Checks if field contains one of {@link FindAll} {@link FindBy} {@link FindBys} annotation
   * @param field field to check
   * @return if one pf annotations is present
   */
  public static boolean isFindByAnnotationPresent(Field field) {
    for (Class<?> annotation : FIND_ANNOTATIONS) {
      if (field.isAnnotationPresent((Class<? extends Annotation>) annotation)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if field contains {@link Global} annotation
   * @param field field to check
   * @return if annotation is present
   */
  public static boolean isGlobal(Field field) {
    return field.isAnnotationPresent(Global.class);
  }
}