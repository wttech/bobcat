---
title: "Cucumber"
---

[Cucumber](https://docs.cucumber.io/) is a third party library, dedicated for Behaviour-Driver Development.

It is possible to run Bobcat with Cucumber, although we have changed the approach in version `2.1.0` - you can read about it more below.

## Background, i.e. where is `bb-cumber` module?

From the very beginning Bobcat had its own module for Cucumber integration, `bb-cumber`, similar to the way we have `bb-junit5`. It provided Cucumber dependencies and few additional utilities we have developed.

Cucumber-JVM had its own limitations, which we have tried to overcome by extending its won runner. This has proven to be problematic in terms of maintainability in the longer run. In addition, we realized we are not doing BDD but using Cucumber as a test runner - we did not see any additional value in our projects, that is why we have started to favor a pure JUnit approach, especially after JUnit 5 has been released.

In the meantime, Cucumber-JVM has introduced few enhancements that previously had to been handled by additional Maven plugins (e.g. parallel runs) and after updating Bobcat with the latest Cucumber release, removing the uncompatible functonalities, we have realized that `bb-cumber` is left with... a single class. Hence, we have decided to scrap the module totally, especially, that the more important part, how to run tests, was handled in the Gradle templates Bobcat provides. 


## Bobcat BDD templates

From `2.1.0` version, Bobcat `bobcat-bdd` and `bobcat-aem-bdd` Gradle templates provide direct dependencies to the Cucumber, namely:
- `cucumber-java`
- `cucumber-java8`
- `cucumber-guice`

Additionally, the following are set up/available to the users:
- `cucumber.properties` that point to the Injector source, so the Bobcat Guice modules are initialized correctly
- `ScenarioContext` - an object for transferring data between states; it follows the same concept as the `world` object in Ruby's bindings of Cucumber
  - you can use it or create your own, more domain-specific object and scrape it
  - for more info, see [this article](http://www.thinkcode.se/blog/2017/08/16/sharing-state-between-steps-in-cucumberjvm-using-guice)
- `cucumber` Gradle task, which is used for running the tests

Important notices:
- we have forfeit using the `cucumber-junit` artifact in favor of the CLI runner provided by Cucumber - the JUnit integration is limited and still based on JUnit4
- we highly recommend using the `Cucumber for Java` plugin in Intellij IDEA, as it will allow you to run your `.feature` files directly from the IDE - thanks to this and due to the above point, there's no need to create additional runners right now! 

In case you prefer to use the `cucumber-junit` approach - feel free to add the dependency and do it, Bobcat does not enforce anything from now on :).

Please refer to Cucumber's official docs in terms of anything Cucumber-related: [link](https://cucumber.io/docs/tools/java/) 

## Migrating from previous versions (`bb-cumber`)

To follow the above approach, do the following:
- `ScenarioContext` migration:
  1. copy the `ScenarioContext` to one of your packages in your codebase; you can grab it from [here](https://raw.githubusercontent.com/Cognifide/bobcat/2.0.3/bb-cumber/src/main/java/com/cognifide/qa/bb/cumber/ScenarioContext.java)
  2. replace all your imports from `import com.cognifide.qa.bb.scenario.ScenarioContext;` to `import <YOUR PACKAGE>.ScenarioContext;`
- replace the dependencies from `bb-cumber` to Cucumber's artifacts:
  ```groovy
  def bobcatVersion = '2.0.3'
  def cucumberVersion = '4.2.6'
  
  //...
  
  dependencies {
      compile group: 'com.cognifide.qa.bb', name: 'bb-core', version: bobcatVersion
      compile "io.cucumber:cucumber-java:$cucumberVersion"
      compile "io.cucumber:cucumber-java8:$cucumberVersion"
      compile "io.cucumber:cucumber-guice:$cucumberVersion"
  }
  ```
- add the following Gradle task and additional configurations:
  ```groovy
  def cucumberArgs = ['--plugin', 'pretty',
                      '--plugin', 'html:build/cucumber-html-report',
                      '--plugin', 'json:build/cucumber-json-report.json',
                      '--glue', 'testing.test',
                      'src/test/resources']
  
  def profiles = [
          'firefox': 'firefox'
  ]
  
  configurations {
      cucumberRuntime {
          extendsFrom testRuntime
      }
  }
  
  task cucumber() {
      def profile = System.getProperty('profile', 'default')
      if (!profile.equals('default')) {
          System.setProperty('bobcat.config.contexts', profile)
      }
  
      dependsOn assemble, compileJava, compileTestJava
      doLast {
          javaexec {
              main = "cucumber.api.cli.Main"
              classpath = configurations.cucumberRuntime + sourceSets.main.output + sourceSets.test.output
              args = cucumberArgs
              systemProperties = System.getProperties()
          }
      }
  }
  ```
- for Maven-based setups (older versions of Bobcat), please refer to Cucumber's docs: [link](https://cucumber.io/docs/tools/java/)

### Migrating `@Transformer` usage
If you have been using the `@Transformer` annotation in your Cucumber step definitions, you might encounter lots of errors. This is due to the fact that it got removed in favor of using a new approach which uses the `TypeRegistryConfigurer`.

For more details, see [official docs](https://cucumber.io/docs/cucumber/configuration/).

## Known limitations

- There is no integration with Allure reporting engine, hence all the additional logging informations provided by Bobcat will not be present in Cucumber's reports
- Bobcumber runner (extension of Cucumber's own runner) handled closing Bobcat's `WebDriverPool`. In case you are re-using `WebDriver` instances (with `webdriver.reusable=true` property), you have to handle this on your own right now.  