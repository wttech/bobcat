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
package com.cognifide.qa.bb.analytics;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AdobeAnalytics implements Analytics {

    private static final String DATA_LAYER_OBJECT_SCRIPT = "return JSON.stringify(DL, null, '\\t')";
    private static final Logger LOG = LoggerFactory.getLogger(AdobeAnalytics.class);
    private static final String JSON = ".json";
    private static final String CFG_PATH = "analytics/";

    @Inject
    private WebDriver driver;

    @Inject
    @Named("adobe.analytics.datalayer")
    private String datalayer;

    @Override public String getActual() {
        return (String) ((JavascriptExecutor) driver).executeScript(DATA_LAYER_OBJECT_SCRIPT.replace("DL", datalayer));
    }

    @Override public String getExpected(String fileName) {
        try {
            URI uri = Resources.getResource(CFG_PATH + fileName + JSON).toURI();
            File file = Paths.get(uri).toFile();
            return FileUtils.readFileToString(file, (String) null);
        } catch (IOException | URISyntaxException e) {
            LOG.error("Could not read JSON file: " + fileName);
        }
        throw new IllegalStateException("JSON file could not be read");
    }

    @Override public void compare(String expectedData) {
        try {
            JSONAssert.assertEquals("Data Layer is incorrect", getExpected(expectedData), getActual(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
