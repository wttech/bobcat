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
package com.cognifide.qa.bb.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import com.cognifide.qa.bb.junit5.allure.EnvironmentInfoExtension;
import com.cognifide.qa.bb.junit5.allure.ScreenshotExtension;
import com.cognifide.qa.bb.junit5.guice.GuiceExtension;
import com.cognifide.qa.bb.junit5.selenium.WebdriverCloseExtension;

@ExtendWith(GuiceExtension.class)
@ExtendWith(ScreenshotExtension.class)
@ExtendWith(WebdriverCloseExtension.class)
@ExtendWith(EnvironmentInfoExtension.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BobcatExtension {
}
