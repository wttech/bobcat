package com.cognifide.qa.bb.api.states.assertions;

import java.util.function.Function;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.api.states.basic.ConditionalState;
import com.cognifide.qa.bb.api.states.State;
import com.cognifide.qa.bb.api.states.basic.StateInObject;

public class Visibility {

  public static <T> State<Boolean> of(Class<T> objectType,
      Function<T, WebElement> elementGetter) {
    StateInObject<T, WebElement> stateInObject = StateInObject.of(objectType, elementGetter);
    return ConditionalState.of(stateInObject, ExpectedConditions::visibilityOf);
  }

  public static State<Boolean> of(WebElement element) {
    return ConditionalState.of(actor -> element, ExpectedConditions::visibilityOf);
  }
}
