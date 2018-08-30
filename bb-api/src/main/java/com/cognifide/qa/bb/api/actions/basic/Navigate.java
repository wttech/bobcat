package com.cognifide.qa.bb.api.actions.basic;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actions.basic.navigate.NavigateBack;
import com.cognifide.qa.bb.api.actions.basic.navigate.NavigateRefresh;
import com.cognifide.qa.bb.api.actions.basic.navigate.NavigateToUrl;

/**
 * Factory class that provides a more human-readable syntax for creating Navigate actions.
 */
public class Navigate {

  public static Action to(String url) {
    return new NavigateToUrl(url);
  }

  public static Action back() {
    return new NavigateBack();
  }

  public static Action refresh() {
    return new NavigateRefresh();
  }
}
