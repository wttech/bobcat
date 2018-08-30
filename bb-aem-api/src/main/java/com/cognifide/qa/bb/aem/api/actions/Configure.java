package com.cognifide.qa.bb.aem.api.actions;

import java.util.Map;

import com.cognifide.qa.bb.aem.api.actions.configure.ConfigureComponent;
import com.cognifide.qa.bb.api.actions.Action;

/**
 * Factory class for providing a human-readable API for creating Configure actions
 */
public class Configure {
  public static Action theComponent(Class component, Map parameters) {
    return new ConfigureComponent(component, parameters);
  }
}
