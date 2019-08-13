---
title: Configuring Bobcat
---

## External configuration elements
### WebDriver
Bobcat uses Selenium and it requires drivers for browsers which we'd like to use for testing. We recommend to add driver executables to your system's PATH environment variable.

For more information, please refet to [official WebDriver docs](https://seleniumhq.github.io/docs/site/en/webdriver/driver_requirements/)

## YAML-based configuration mechanism

Since 2.0.0 version, Bobcat uses the new configuration model: YAML-based approach.

### Configuration structure

It is based on a single YAML file, placed at the root of your resources folder, named `config.yaml`.
The configuration file has the following structure:

```yaml
default: # this part contains all your properties that are loaded by default
  properties:
    property1: value1
    property2: value2
    # ...
  contexts: [context1, context2] # this property tells Bobcat which contexts to activate and load by default
                                 # you can override this choice with bobcat.config.contexts system property
                                 # Warning! You always have to provide the whole list!
contexts:
  context1:
    property1: value1
    property3: value3
  context2:
    property4: value4
  context3:
    property1: yet-another-value
    property2: a-value
  # ...    
```

Contexts are basically groups of properties that can be activated together. What are the possible use cases? For example your browser-specific WebDrivers configurations - you can have one context for a remote Selenium Grid, second for a local ChromeDriver, while the next two contain a set of properties defining your development environments, e.g. integration and staging.

#### Example configuration

```yaml
default:
  properties:
    webdriver.type: remote
    webdriver.url: http://1.2.3.4:4444/wd/hub
    webdriver.cap.recordVideo: false
    webdriver.cap.maxInstances: 1
    webdriver.cap.platform: LINUX
  contexts: [dev, chrome]

contexts:
  chrome:
    webdriver.cap.browserName: chrome
  firefox:
    webdriver.cap.browserName: firefox
  dev:
    author.url: https://dev-author.projectname.com
    author.ip: http://1.2.3.4:4502
    author.login: admin
    author.password: ""
    publish.url: https://dev.projectname.com
    publish.ip: http://1.2.3.4:4503
  prod:
    author.url: https://author.projectname.com
    author.ip: http://1.2.3.1:4502
    author.login: bobcat
    author.password: ""
    publish.url: https://projectname.com
    publish.ip: http://1.2.3.1:4503
```

### Overrides

Properties from contexts can override your default ones (or default Bobcat values), in case of the following config:
```yaml
default:
  properties:
    property1: to-be-overriden
    contexts: [context1]
contexts:
  context1:
    propery1: override
```
`property1` will end up with value `override`.

### Additional contexts in separate files

When having many properties to manage (e.g. a long list of page URLs), keeping all that info in a single file might become a bit cumbersome. That is why Bobcat allows you to load additional sets of data from separate files.

Bobcat will look for a `contexts` folder under your resources and load all YAML files that are there (you can group those contexts easily in folders - Bobcat will scan the whole directory). Such files should have the following structure:

```yaml
context-name1:
  property1: value1
context-name2:
  property2: value2
```

Bobcat will treat them as regular contexts in the main config file - all you have to do, to load them is to either add them to the `contexts` list in `config.yaml` or provide them via command line by setting the `bobcat.config.contexts` System property.

## How Bobcat resolves its configuration

In general algorithm for resolving Bobcat configuration is following (you can look up the implementation in `ConfigStrategy#gatherProperties`):

1. Read the default properties, defined in Bobcat internals.
2. Read the user configuration, defined in your project.
3. Override loaded properties with any properties passed in command line.
4. Set System properties so they can be used by WebDriver internals.

## Using the defined properties throughout your tests

You can store not only Bobcat-specific properties in your configurations. You can define and load the same way anything that is related to your tests, e.g. your pages URLs.

### Named injections

To retrieve a property, you can use Guice's `@Inject` annotation with additional `@Named` annotation.
As a parameter for `@Named` annotation, you use the name of the property you want to inject, just as defined in your config file.
Here is an example:
```java
import com.google.inject.Inject;
import com.google.inject.name.Named;
//..
@Inject
@Named("homepage.url")
private String homepageUrl;
//..
```

### Injecting Properties class

It is also possible to inject the Properties instance. This instance is initialized by Bobcat and contains all the properties that Bobcat found in your configuration file.

Example:
```java
@Test
public class MyTest{
    // ...
 
    @Inject
    private Properties properties;
 
    // ...
}
```

Example usage:
```java
public void myTestCase() {
//..
   String homepageUrl = (String) properties.get("homepage.url");
   bobcatWait.withTimeout(Timeouts.MEDIUM).until(
       ExpectedConditions.urlToBe(homepageUrl));
 //..
}
```

The following code is responsible for getting property object of given key:
```java
properties.get("homepage.url")
```

## Available properties

Below you can find the list of properties available in Bobcat out-of-the-box:

| Property | Default value | Comments |
|----------|---------------|----------|
| `webdriver.type` | not set | Determines which type of WebDriver is created |
| `webdriver.maximize` | false | Maximizes the browser window |
| `webdriver.reusable` | false | Enables reusability of `WebDriver` instances: instead of closing, browser's cookies are being cleaned up; potentially useful when creating WebDriver instances is resource-consuming|
| `webdriver.mobile` | false | Denotes the driver is mobile. Disables alert checking when `webdriver.reusable` is set. |
| `webdriver.url` | '' | URL of the Selenium Grid; used when `webdriver.type` is set to `remote` |
| `webdriver.chrome.headless` | false | Chrome-specific; enables headless mode |
| `webdriver.chrome.acceptInsecureCerts` | false | Chrome-specific; allows accepting insecure certificates |
| `cookies.loadAutomatically` | true | Determines if the defined cookies are loaded automatically by Bobcat; more info: [link]({{site.baseurl}}/core/cookies/) |
| `cookies.file` | not set | Determines the file with cookies definition; more info: [link]({{site.baseurl}}/core/cookies/) |
| `modifiers.implicitTimeout` | true | Sets the implicit timeout to the value set in `timings.implicitTimeout` |
| `timings.explicitTimeout` | 10 | Value of default explicit timeout |
| `timings.implicitTimeout` | 1 | Value of default implicit timeout |
| `timings.pollingInterval` | 500 | Value of default polling interval |
| `proxy.enabled` | false | Controls if the BrowserMob proxy is enabled; enables it for the whole traffic |
| `proxy.ip` | 127.0.0.1 | IP of the proxy service |
| `proxy.port` | 9000 | Port of the proxy service |
| `analytics.uri.prefix` | /b/ss | `bb-traffic`-specific; determines URI prefix for AnalyticsWait |
| `analytics.call.timeout.ms` | 10000 | Defines default timeout for analytics calls in AnalyticsWait |
| `login.token.name` | login-token | AEM-specific: defines the name of authentication cookie |