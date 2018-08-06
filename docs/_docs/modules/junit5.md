---
title: "Module: JUnit 5"
---

Available since 1.5.0
{: .notice--info}

JUnit 5 module allows to run test using JUnit 5 platform
It also allow to generate allure reports which is included in this module

1. Add dependency to your `build.gradle` file:

    ```gradle
       compile group: 'com.cognifide.qa.bb', name: 'bb-junit5', version: 1.5.0
    ```
2. Add JUnit platform to test task
    ```gradle
      test {
        useJUnitPlatform()
        systemProperties = System.getProperties()
        systemProperty 'junit.jupiter.extensions.autodetection.enabled', 'true'
        maxParallelForks = 3    
    }
    ```
3. Mark test methods with JUnit 5 test annotation
4. Mark test classes with @RunWithJunit5 

This module uses [Allure](http://allure.qatools.ru/) for reports. Full documentation can be found [here]({{site.baseurl}}/docs/allure/) 
