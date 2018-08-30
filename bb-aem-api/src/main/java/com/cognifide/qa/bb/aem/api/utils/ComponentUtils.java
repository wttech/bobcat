package com.cognifide.qa.bb.aem.api.utils;

import java.lang.annotation.Annotation;

import com.cognifide.qa.bb.aem.api.Dialog;
import com.cognifide.qa.bb.aem.api.Hoverbar;
import com.cognifide.qa.bb.aem.api.qualifiers.Component;

public class ComponentUtils {
  public static String getName(Class component) {
    return getAnnotation(component).name();
  }

  public static Class<? extends Dialog> getDialog(Class component) {
    return getAnnotation(component).dialog();
  }

  public static Class<? extends Hoverbar> getHoverbar(Class component) {
    return getAnnotation(component).hoverbar();
  }

  private static Component getAnnotation(Class component) {
    Annotation componentAnnotation = component.getAnnotation(Component.class);
    if (componentAnnotation instanceof Component) {
      return (Component) componentAnnotation;
    } else {
      throw new IllegalArgumentException(component
          + " is not a valid Component! Either decorate your Page Object with @Component or provide a valid type.");
    }
  }
}
