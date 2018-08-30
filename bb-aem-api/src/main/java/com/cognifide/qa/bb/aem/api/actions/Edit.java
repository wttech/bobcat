package com.cognifide.qa.bb.aem.api.actions;

import com.cognifide.qa.bb.aem.api.actions.edit.EditComponent;
import com.cognifide.qa.bb.api.actions.Action;

/**
 * Factory class that exposes a more human-readable API to provide Edit actions.
 */
public class Edit {

  public static Action component(Class component) {
    return new EditComponent(component);
  }
}
