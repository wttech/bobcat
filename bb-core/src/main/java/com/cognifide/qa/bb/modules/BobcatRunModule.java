/*-
 * #%L
 * Bobcat
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
package com.cognifide.qa.bb.modules;

import com.cognifide.qa.bb.utils.YamlReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BobcatRunModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(BobcatRunModule.class);

  @Override
  protected void configure() {
    String runmodes = System.getProperty("runmode", "default");
    String[] separateRunModes = StringUtils.split(runmodes, ",");
    List<String> modules = new ArrayList<>();
    TypeReference typeReference = new TypeReference<List<String>>() {
    };
    Arrays.stream(separateRunModes).forEach(runmode ->
        modules.addAll(YamlReader.readFromTestResources("runmodes/" + runmode, typeReference))
    );
    modules.stream().forEach(this::installFromName);
  }

  private void installFromName(String moduleName) {
    try {
      install((Module) Class.forName(moduleName).newInstance());
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      LOG.error("Error when binding module: " + moduleName, e);
    }
  }

}
