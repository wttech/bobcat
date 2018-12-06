---
title: "Module: Email"
---

{% include under-construction.html %} construction

To get Email module to be ready to use follow these two steps:

1. Add dependency into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-email', version: '<Bobcat Version>'
    }
    ```
2. Installation:
    To install this module add following into your runmode file:

     ```yaml
     - com.cognifide.qa.bb.email.EmailModule
     ```
