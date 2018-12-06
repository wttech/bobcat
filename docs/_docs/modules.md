---
title: Bobcat modules
---

Bobcat has a modular structure, meaning you can choose what modules you want to use at any time.

## Using Bobcat modules in your project

To use any module on the list below you need to do two things:

1. Make sure you added the module as a dependency to your project.
    
    In Gradle that would mean adding it in the project's dependencies:
    ```groovy
    compile group: 'com.cognifide.qa.bb', name: 'bb-core', version: '2.0.1'
    ```
2. You need to add module to your runmode file.

    Wait, what?

    Usually, each Bobcat module has its own dedicated Guice module - you need to add them to any runmode file you wanto to use it (more information can be found in [here]({{site.baseurl}}/docs/modules/core/runmodes/)). If you use a template then it already has got all required modules.
    Find `resources/runmodes/default.yaml` (or other yaml file in runmodes folder if you want to use different runmode)
    
    ```yaml
    - com.cognifide.qa.bb.modules.CoreModule
    ```  
    
    That will cause `BobcatRunModule.class` that should be added to every test to find these modules and install them
    
    The old way (using `GuiceModule` will still works however we recommend the new approach)
    {: .notice--info}

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

### Module: AEM Core

This module contains common interfaces and utilities for AEM Authoring tests

More about its features: [here]({{site.baseurl}}/docs/modules/aem-core/)
{: .notice--info}

### Module: AEM 64

This module contains implementation of AEM Core interfaces and utils for AEM 6.4 testing

More about its features: [here]({{site.baseurl}}/docs/modules/aem-64/)
{: .notice--info}