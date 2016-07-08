/*-
 * #%L
 * Bobcat Parent
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
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
package com.cognifide.qa.bb.actions;



import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Purpose of this interceptor is altering all Actions::sendKeys calls with argument character sequences to
 * one-character calls, for example sendKeys("ABC") to sequence of sendKeys('A').sendKeys('B').sendKeys('C')
 * to avoid <a href="https://github.com/SeleniumHQ/selenium-google-code-issue-archive/issues/4446">4446</a>
 * issue.
 * <br>
 * If the char sequence contains special keys or are not from UTF-8 encoding then call's arguments are
 * passed through.
 */
final class SendKeysByOneActionsInterceptor implements MethodInterceptor {

  private static final String SEND_KEYS_METHOD_NAME = "sendKeys";

  private final Actions actions;

  private SendKeysByOneActionsInterceptor(Actions actions) {
    this.actions = actions;
  }

  @Override
  public Object intercept(Object thiz, Method method, Object[] args, MethodProxy methodProxy)
      throws Throwable {
    if (SEND_KEYS_METHOD_NAME.equals(method.getName())) {
      WebElement webElement = webElementFromArguments(args);
      Arrays.asList(args).stream()
          .filter(o -> o instanceof CharSequence[]).map(o -> (CharSequence[]) o).flatMap(Stream::of)
          .flatMap(c -> splitIntoCharsIfPossible(c))
          .forEach(c -> actions.sendKeys(webElement, c));
    } else {
      method.invoke(actions, args);
    }
    return thiz;
  }

  static Actions wrap(Actions actions) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(Actions.class);
    enhancer.setCallback(new SendKeysByOneActionsInterceptor(actions));
    return (Actions) enhancer
        .create(new Class[] {Keyboard.class, Mouse.class}, new Object[] {null, null});
  }

  private Stream<CharSequence> splitIntoCharsIfPossible(CharSequence input) {
    return isEmpty(input) || firstCharIsKey(input) ? Stream.of(input) : sequenceToStream(input);
  }

  private WebElement webElementFromArguments(Object[] args) {
    return (WebElement) Stream.of(args).findFirst()
        .filter(o -> o instanceof WebElement).orElse(null);
  }

  private boolean isEmpty(CharSequence charSequence) {
    return charSequence.length() == 0;
  }

  private boolean firstCharIsKey(CharSequence charSequence) {
    return charSequence instanceof Keys || isKeysChord(charSequence.charAt(0));
  }

  private boolean isKeysChord(final char character) {
    return null != Keys.getKeyFromUnicode(character);
  }

  private Stream<CharSequence> sequenceToStream(CharSequence charSequence) {
    return charSequence.chars().mapToObj(c -> String.valueOf((char) c));
  }
}
