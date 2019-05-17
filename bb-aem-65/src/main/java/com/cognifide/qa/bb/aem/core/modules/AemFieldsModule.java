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

  @Override
  protected void configure() {
    bind(ControlToolbar.class).to(ControlToolbarImpl.class);
    bind(JustifyControls.class).to(JustifyControlsImpl.class);
    bind(ListControls.class).to(ListControlsImpl.class);

    MapBinder<String, DialogField> fieldsBinder = MapBinder
        .newMapBinder(binder(), String.class, DialogField.class);
    fieldsBinder.addBinding(Fields.CHECKBOX).to(Checkbox.class);
    fieldsBinder.addBinding(Fields.TEXTFIELD).to(Textfield.class);
    fieldsBinder.addBinding(Fields.IMAGE).to(Image.class);
    fieldsBinder.addBinding(Fields.PATHBROWSER).to(PathBrowser.class);
    fieldsBinder.addBinding(Fields.SELECT).to(Select.class);
    fieldsBinder.addBinding(Fields.RICHTEXT).to(RichText.class);
    fieldsBinder.addBinding(Fields.MULTIFIELD).to(Multifield.class);
    fieldsBinder.addBinding(Fields.MULTIFIELD_ITEM).to(MultifieldItem.class);
    fieldsBinder.addBinding(Fields.RICHTEXT_FONT_FORMAT).to(FontFormat.class);
    fieldsBinder.addBinding(Fields.RICHTEXT_JUSTIFY).to(JustifyDialogPanel.class);
    fieldsBinder.addBinding(Fields.RICHTEXT_LIST).to(ListDialogPanel.class);
    fieldsBinder.addBinding(Fields.RADIO_GROUP).to(RadioGroup.class);
    fieldsBinder.addBinding(Fields.RADIO_GROUP_MULTI).to(RadioGroup.class);
  }
}
