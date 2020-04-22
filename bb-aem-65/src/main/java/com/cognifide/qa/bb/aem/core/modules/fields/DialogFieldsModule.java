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
package com.cognifide.qa.bb.aem.core.modules.fields;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Checkbox;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.ContentFragmentPathBrowser;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultContentFragmentPathBrowser;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultCheckbox;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultImage;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultMultifield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultMultifieldItem;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultNumberInput;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultPathBrowser;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultRadioGroup;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultRichText;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultSelect;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DefaultTextfield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Image;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Multifield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.MultifieldItem;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.NumberInput;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.PathBrowser;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.RadioGroup;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.RichText;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Select;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Textfield;
import com.google.inject.AbstractModule;

/**
 * This module contains bindings for TouchUI dialog fields
 */
public class DialogFieldsModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(DialogFieldsModule.class);

  @Override
  protected void configure() {
    LOG.debug("Configuring Bobcat module: {}", getClass().getSimpleName());

    bind(Checkbox.class).to(DefaultCheckbox.class);
    bind(Image.class).to(DefaultImage.class);
    bind(Multifield.class).to(DefaultMultifield.class);
    bind(MultifieldItem.class).to(DefaultMultifieldItem.class);
    bind(PathBrowser.class).to(DefaultPathBrowser.class);
    bind(RadioGroup.class).to(DefaultRadioGroup.class);
    bind(RichText.class).to(DefaultRichText.class);
    bind(Select.class).to(DefaultSelect.class);
    bind(Textfield.class).to(DefaultTextfield.class);
    bind(NumberInput.class).to(DefaultNumberInput.class);
    bind(ContentFragmentPathBrowser.class).to(DefaultContentFragmentPathBrowser.class);

    install(new FieldsRegistryModule());
  }
}
