package com.cognifide.qa.bb.aem.authoring;

import java.util.function.Function;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.api.Actor;
import com.cognifide.qa.bb.aem.api.State;
import com.cognifide.qa.bb.aem.authoring.actors.PerformBasicOperations;

public class WebElementState<T, R> implements State<R> {
  private Function<T, WebElement> elementGetter;
  private Class<T> objectType;
  private Function<WebElement, R> webElementProp;

  private WebElementState(Class<T> objectType, Function<T, WebElement> elementGetter,
      Function<WebElement, R> webElementProp) {
    this.objectType = objectType;
    this.elementGetter = elementGetter;
    this.webElementProp = webElementProp;
  }

  @Override
  public R observedBy(Actor actor) {
    return elementGetter.andThen(webElementProp)
        .apply(actor.can(PerformBasicOperations.class).instantiate(objectType));
  }

  public static <T, R> State<R> of(Class<T> objectType, Function<T, WebElement> elementGetter,
      Function<WebElement, R> webElementProp) {
    return new WebElementState<>(objectType, elementGetter, webElementProp);
  }
}
