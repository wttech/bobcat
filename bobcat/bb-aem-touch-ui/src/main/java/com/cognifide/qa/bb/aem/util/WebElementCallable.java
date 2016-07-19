package com.cognifide.qa.bb.aem.util;

import org.openqa.selenium.WebElement;

public interface WebElementCallable<T> {
  T call(WebElement element);
}
