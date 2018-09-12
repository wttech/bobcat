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
package com.cognifide.qa.bb.aem.core.guice;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Checkbox;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.FieldType;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Image;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Multifield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.MultifieldItem;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.PathBrowser;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.RichText;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Select;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Textfield;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Variant;
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
 * This module contains bindings to touch ui dialog fields
 */
public class AemFieldsModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ControlToolbar.class).to(ControlToolbarImpl.class);
    bind(JustifyControls.class).to(JustifyControlsImpl.class);
    bind(ListControls.class).to(ListControlsImpl.class);

    MapBinder<String, DialogField> fieldsBinder = MapBinder
        .newMapBinder(binder(), String.class, DialogField.class);
    fieldsBinder.addBinding(FieldType.CHECKBOX.name()).to(Checkbox.class);
    fieldsBinder.addBinding(FieldType.TEXTFIELD.name()).to(Textfield.class);
    fieldsBinder.addBinding(FieldType.IMAGE.name()).to(Image.class);
    fieldsBinder.addBinding(FieldType.PATHBROWSER.name()).to(PathBrowser.class);
    fieldsBinder.addBinding(FieldType.SELECT.name()).to(Select.class);
    fieldsBinder.addBinding(FieldType.RICHTEXT.name()).to(RichText.class);
    fieldsBinder.addBinding(FieldType.VARIANT.name()).to(Variant.class);
    fieldsBinder.addBinding(FieldType.MULTIFIELD.name()).to(Multifield.class);
    fieldsBinder.addBinding(FieldType.MULTIFIELD_ITEM.name()).to(MultifieldItem.class);
    fieldsBinder.addBinding(FieldType.RICHTEXT_FONT_FORMAT.name()).to(FontFormat.class);
    fieldsBinder.addBinding(FieldType.RICHTEXT_JUSTIFY.name()).to(JustifyDialogPanel.class);
    fieldsBinder.addBinding(FieldType.RICHTEXT_LIST.name()).to(ListDialogPanel.class);
  }
}
