---
title: Bobcat modules
---

Bobcat has a modular structure, meaning you can choose what modules you want to use at any time.

## Using Bobcat modules in your project

To use any module on the list below you need to do two things:

1. Make sure you added the module as a dependency to your project.
    
    In Maven that would mean adding it in the project's `pom.xml` file:
    ```xml
    <dependency>
        <groupId>com.cognifide.qa.bb</groupId>
        <artifactId>bb-core</artifactId>
        <version>1.4.0</version>
    </dependency>
    ```
2. You need to 'install' the module's Guice module.

    Wait, what?

    Usually, each Bobcat module has its own dedicated Guice module - you need to install them in your aggregated project one. By default, when using our project template, you will have a `GuiceModule` in your project. Over there, you need to add the following line:
    ```java
    public class GuiceModule extends AbstractModule {
        @Override
        protected void configure() {
            // ...

            install(new CoreModule()); //you can find the name in respective module's documentation

            // `other modules
        }
    }
    ```

## Modules

Below is the list of all available modules.

### Module: Core

This module is the heart of Bobcat. No matter which runner or technology you are working with, to use Bobcat, you need to grab the Core module.

More about its features: [here]({{site.baseurl}}/docs/modules/core/)
{: .notice--info}

### Module: JUnit5

This module is responsible for executing tests in JUnit5.

More about its features: [here]({{site.baseurl}}/docs/modules/junit5/)
{: .notice--info}

### Module: Cucumber

This module enables you to run your tests with Cucumber JVM.

More about its features: [here]({{site.baseurl}}/docs/modules/cucumber/)
{: .notice--info}

### Module: Traffic

This module enables grabbing and analyzing the HTTP traffic in your tests. It uses the BrowserMob proxy.

More about its features: [here]({{site.baseurl}}/docs/modules/traffic/)
{: .notice--info}

### Module: Email

This module contains useful utilities for working with emails.

More about its features: [here]({{site.baseurl}}/docs/modules/email/)
{: .notice--info}

### Module: AEM Common

This module contains a bunch of handy helpers when working in AEM in general.

More about its features: [here]({{site.baseurl}}/docs/modules/aem-common/)
{: .notice--info}

----

## Deprecated modules

These modules are not supported anymore - you might still grab and use the latest version available.

### Module: JUnit

>Latest available version: `1.6.0`

This module is responsible for executing tests in JUnit 4.

More about its features: [here]({{site.baseurl}}/docs/modules/junit/)
{: .notice--info}

### Module: Reports

>Latest available version: `1.6.0`

This module provides Bobcat's reporting engine which is dedicated for running tests with JUnit.

More about its features: [here]({{site.baseurl}}/docs/modules/reports/)
{: .notice--info}

### Module: AEM Touch UI

>Latest available version: `1.6.0`

This module comes with ready page objects that may be utilized to automate AEM's Touch UI authoring interface.

More about its features: [here]({{site.baseurl}}/docs/modules/aem-touch-ui/)
{: .notice--info}

### Module: AEM Classic

>Latest available version: `1.6.0`

This module is dedicated for working with AEM's Classic UI.

More about its features: [here]({{site.baseurl}}/docs/modules/aem-classic-ui/)
{: .notice--info}