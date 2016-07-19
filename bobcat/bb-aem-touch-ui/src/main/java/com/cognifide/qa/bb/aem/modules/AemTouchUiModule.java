package com.cognifide.qa.bb.aem.modules;

import com.cognifide.qa.bb.aem.data.components.Components;
import com.cognifide.qa.bb.aem.data.components.ComponentsProvider;
import com.cognifide.qa.bb.aem.data.pages.Pages;
import com.cognifide.qa.bb.aem.data.pages.PagesProvider;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.aem.pageobjects.pages.PublishPageFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class AemTouchUiModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FieldsModule());
    bind(Components.class).toProvider(ComponentsProvider.class);
    bind(Pages.class).toProvider(PagesProvider.class);

    install(new FactoryModuleBuilder().build(AuthorPageFactory.class));
    install(new FactoryModuleBuilder().build(PublishPageFactory.class));
  }

}
