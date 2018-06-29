/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
