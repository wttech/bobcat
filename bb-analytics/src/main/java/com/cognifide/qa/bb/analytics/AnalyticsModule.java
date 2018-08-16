package com.cognifide.qa.bb.analytics;

import com.google.inject.AbstractModule;

public class AnalyticsModule extends AbstractModule {

    @Override protected void configure() {
        bind(Analytics.class).toProvider(AdobeAnalyticsProvider.class);
    }
}
