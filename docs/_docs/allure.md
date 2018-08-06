---
title: "Allure Reports"
---

[Allure](http://allure.qatools.ru/) is a third party report engine we are using in JUnit 5 module.

## Description 

It is by default enabled in our JUnit 5 template and all Allure annotations are available. Template also prepares report after test task.
There are two properties to control allure integration:

```
  allure.report (default is false set true in template)
``` 
This property is required to run all bobcat allure features (automated screenshots and enviroment file)

```
  allure.create.enviroment (default is false set true in template)
```
This property turns on automated environment properties file to be prepared by Bobcat

## Manual installation

1. Add dependencies and tasks to gradle file already prepared for [JUnit 5]({{site.baseurl}}/docs/modules/junit5/) 
    ```gradle
      (...)
      buildscript {
          repositories {
              jcenter()
              mavenLocal()
              mavenCentral()
          }
          dependencies {
              classpath 'io.qameta.allure:allure-gradle:2.5'
          }
      }
  
      (...)
      apply plugin: 'io.qameta.allure'
  
      (...)
      gradle.projectsEvaluated {
          test.finalizedBy allureReport
      }
  
      allure {
          version = '2.7.0'
          autoconfigure = true
          aspectjweaver = true
          aspectjVersion = '1.8.10'
      
          useJUnit5 {
              version = '2.7.0'
          }
      }
    ```
    
    Read more about Allure Gradle plugin configuration on their official [documentation](https://github.com/allure-framework/allure-gradle).
    
2. Set properties. Both are optional but we recommend turning at least allure.report to allow taking screenshots on test failure
```
  allure.report: true
  allure.create.enviroment: true
``` 
  
