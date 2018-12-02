---
title: "Module: Cucumber"
---

Under {% include under-construction.html %}

To get Cucumber module to be ready to use follow these two steps:

1. Add dependency into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-cumber', version: '<Bobcat Version>'
    }
    ```
2. Installation:
    
    To install this module add following into your runmode file:

     ```yaml
     - com.cognifide.qa.bb.cumber.guice.BobcumberModule
     ```
