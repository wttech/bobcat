---
title: "Module: Core"
---

As mentioned in Modules description, Core is the heart of Bobcat. To configure it in your project you'll obligatory need these two steps:
1. Add dependency into your `pom.xml` file:

    ```xml
    <dependency>
            <groupId>com.cognifide.qa.bb</groupId>
            <artifactId>bb-core</artifactId>
            <version>1.4.0</version>
        </dependency>
    ```
2. Installation:
To install this module add following into your `GuiceModule` file:

     ```java
        public class GuiceModule extends AbstractModule {
            @Override
            protected void configure() {
                // ...
    
                install(new CoreModule()); 
            }
        }
      ```