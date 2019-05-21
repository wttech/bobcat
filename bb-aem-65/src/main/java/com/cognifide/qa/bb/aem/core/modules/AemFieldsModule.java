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
package com.cognifide.qa.bb.aem.core.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Checkbox;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Fields;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Image;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Multifield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.MultifieldItem;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.PathBrowser;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.RadioGroup;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.RichText;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Select;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Textfield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.ControlToolbar;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.ControlToolbarImpl;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.FontFormat;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.JustifyControls;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.JustifyControlsImpl;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.JustifyDialogPanel;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.ListControls;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.ListControlsImpl;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.ListDialogPanel;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * This module contains bindings for TouchUI dialog fields
 */
public class AemFieldsModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(AemFieldsModule.class);

  @Override
  protected void configure() {
    LOG.debug("Configuring Bobcat module: {}", getClass().getSimpleName());

    bind(ControlToolbar.class).to(ControlToolbarImpl.class);
    bind(JustifyControls.class).to(JustifyControlsImpl.class);
    bind(ListControls.class).to(ListControlsImpl.class);

    LOG.debug("Registering dialog fields...");
    MapBinder<String, DialogField> fieldsBinder = MapBinder
        .newMapBinder(binder(), String.class, DialogField.class);
    registerField(fieldsBinder, Fields.CHECKBOX, Checkbox.class);
    registerField(fieldsBinder, Fields.TEXTFIELD, Textfield.class);
    registerField(fieldsBinder, Fields.IMAGE, Image.class);
    registerField(fieldsBinder, Fields.PATHBROWSER, PathBrowser.class);
    registerField(fieldsBinder, Fields.SELECT, Select.class);
    registerField(fieldsBinder, Fields.RICHTEXT, RichText.class);
    registerField(fieldsBinder, Fields.MULTIFIELD, Multifield.class);
    registerField(fieldsBinder, Fields.MULTIFIELD_ITEM, MultifieldItem.class);
    registerField(fieldsBinder, Fields.RICHTEXT_FONT_FORMAT, FontFormat.class);
    registerField(fieldsBinder, Fields.RICHTEXT_JUSTIFY, JustifyDialogPanel.class);
    registerField(fieldsBinder, Fields.RICHTEXT_LIST, ListDialogPanel.class);
    registerField(fieldsBinder, Fields.RADIO_GROUP, RadioGroup.class);
  }

  private void registerField(MapBinder<String, DialogField> binder, String name,
      Class<? extends DialogField> type) {
    LOG.debug("Registering field: {} [{}]", name, type);
    binder.addBinding(name).to(type);
  }
}
