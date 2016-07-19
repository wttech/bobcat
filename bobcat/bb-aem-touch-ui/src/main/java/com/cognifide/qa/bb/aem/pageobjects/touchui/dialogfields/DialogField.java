package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;

public interface DialogField {

  void setValue(Object value);

  FieldType getType();
}
