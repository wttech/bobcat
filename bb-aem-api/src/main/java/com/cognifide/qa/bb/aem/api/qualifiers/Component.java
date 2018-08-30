package com.cognifide.qa.bb.aem.api.qualifiers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cognifide.qa.bb.aem.api.Dialog;
import com.cognifide.qa.bb.aem.api.Hoverbar;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
  String name();

  Class<? extends Dialog> dialog();

  Class<? extends Hoverbar> hoverbar();
}
