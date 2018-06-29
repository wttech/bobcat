package com.cognifide.qa.bb.eyes;

import com.applitools.eyes.selenium.Eyes;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

@ThreadScoped
public class EyesProvider implements Provider<Eyes> {

  private Eyes cachedEyes;

  @Inject
  @Named("eyes.apiKey")
  private String apiKey;

  @Inject
  @Named("eyes.fullPageScreenshots")
  private boolean fullPageScreenshots;

  @Override
  public Eyes get() {
    if (cachedEyes == null) {
      cachedEyes = create();
    }
    return cachedEyes;
  }

  private Eyes create() {
    Eyes eyes = new Eyes();
    eyes.setApiKey(apiKey);
    eyes.setForceFullPageScreenshot(fullPageScreenshots);
    return eyes;
  }
}
