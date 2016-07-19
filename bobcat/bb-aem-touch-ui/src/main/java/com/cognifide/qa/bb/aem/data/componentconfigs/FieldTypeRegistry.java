package com.cognifide.qa.bb.aem.data.componentconfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.DialogField;
import com.google.inject.Inject;

public class FieldTypeRegistry {
  private Map<FieldType, DialogField> registry;

  @Inject
  public FieldTypeRegistry(Set<DialogField> dialogFields) {
    registry = new HashMap<>();
    dialogFields.stream() //
        .forEach(dialogField -> register(dialogField.getType(), dialogField));
  }

  private void register(FieldType key, DialogField value) {
    registry.put(key, value);
  }

  public Class<?> getClass(FieldType type) {
    return registry.get(type).getClass().getSuperclass();
  }
}
