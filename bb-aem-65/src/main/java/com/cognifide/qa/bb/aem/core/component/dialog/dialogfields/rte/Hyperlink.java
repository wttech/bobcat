/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.rte;

import static com.google.common.base.Splitter.on;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.javascriptexecutor.JsScripts;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * Describes Hyperlink widget button in RTE toolbar
 */
@PageObject(css = ".rte-ui")
public class Hyperlink implements DialogField {

  @VisibleForTesting
  protected static final String PATH_PREFIX = "Path";
  @VisibleForTesting
  protected static final String ALT_PREFIX = "Alt Text";
  @VisibleForTesting
  protected static final String TARGET_PREFIX = "Target";

  @FindBy(css = Options.TOOLBAR_ITEM_CSS + "[data-action='links#modifylink']")
  private WebElement listsButton;

  @FindPageObject
  private HyperlinkPopover hyperlinkPopover;

  @Inject
  private JavascriptExecutor javascriptExecutor;

  @Inject
  private BobcatWait wait;

  @Override
  public void setValue(Object value) {
    javascriptExecutor.executeScript(JsScripts.SELECT_ALL);
    wait.until(visibilityOf(listsButton)).click();

    Map<String, Function<String, Object>> mapping = Maps.newHashMap();
    mapping.put(PATH_PREFIX, text -> hyperlinkPopover.enterPath(text));
    mapping.put(ALT_PREFIX, text -> hyperlinkPopover.enterAltText(text));
    mapping.put(TARGET_PREFIX, text -> hyperlinkPopover.setTarget(text));

    validateParsedMap(parseValue((String) value)).forEach(
        (prefix, text) -> mapping.get(prefix).apply(text)
    );

    hyperlinkPopover.confirm();
  }

  @SuppressWarnings("UnstableApiUsage")
  @VisibleForTesting
  protected Map<String, String> parseValue(String value) {
    return on(Pattern.compile("\\n"))
        .withKeyValueSeparator(on(":").trimResults())
        .split(value);
  }

  @VisibleForTesting
  protected Map<String, String> validateParsedMap(Map<String, String> map) {
    List<String> allowedKeys = Arrays.asList(PATH_PREFIX, ALT_PREFIX, TARGET_PREFIX);
    if (!allowedKeys.containsAll(map.keySet())) {
      throw new IllegalArgumentException(String.format(
          "Parsed map contained keys that did not match the allowed prefixes. Key set=%s, allowed prefixes=%s",
          map.keySet(), allowedKeys));
    }
    return map;
  }
}
