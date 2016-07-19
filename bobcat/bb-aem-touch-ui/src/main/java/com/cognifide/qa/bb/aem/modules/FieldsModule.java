package com.cognifide.qa.bb.aem.modules;

import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.Checkbox;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.Image;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.Multifield;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.MultifieldItem;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.PathBrowser;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.RichText;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.Select;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.Textfield;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.Variant;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class FieldsModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<DialogField> fieldsBinder = Multibinder.newSetBinder(binder(), DialogField.class);
    fieldsBinder.addBinding().to(Checkbox.class);
    fieldsBinder.addBinding().to(Textfield.class);
    fieldsBinder.addBinding().to(Image.class);
    fieldsBinder.addBinding().to(PathBrowser.class);
    fieldsBinder.addBinding().to(Select.class);
    fieldsBinder.addBinding().to(RichText.class);
    fieldsBinder.addBinding().to(Variant.class);
    fieldsBinder.addBinding().to(Multifield.class);
    fieldsBinder.addBinding().to(MultifieldItem.class);
  }
}
