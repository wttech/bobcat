---
title: "WebDriver"
---

Creators API is available in `bb-core` since version `2.2.0`
{: .notice--info}

## Configuring browser type

To select which browser (and its corresponding driver) to use, you need to specify it via `webdriver.type` property.

Currently available are following drivers:
- `firefox`
- `chrome`
- `ie`
- `edge`
- `safari`
- `remote`

`bb-appium` module provides two additional ones:
- `android`
- `ios` 

See [official WebDriver docs](https://seleniumhq.github.io/docs/site/en/getting_started_with_webdriver/browsers/) about driver details.

## Configuring browser-specific Capabilities

Controlling the browser or driver options is done via appropriate Capabilities implementation.
In latest versions of WebDriver, the recommended way is to use dedicated browser classes:

| Browser | Capabilities |
|---------|--------------|
| Firefox | `FirefoxOptions` |
| Chrome | `ChromeOptions` |
| Edge | `EdgeOptions` |
| IE | `InternetExplorerOptions` |
| Safari | `SafariOptions` |
| RemoteDriver (Selenium Grid) | `DesiredCapabilities` |

For a list of available setting and options, please refer to each driver's docs.

Few settings exposed directly in Bobcat, see more [here]({{site.baseurl}}/docs/configuring-bobcat/).

### Adding custom Capabilities
You can modify the capabilities using the extension point in form of the `CapabilityModifier` interface - implement its methods and register it in your Guice module.

Example `CapabilityModifier` implementation:

```java
public class EnableProxy implements CapabilitiesModifier {

  @Inject
  @Named(ConfigKeys.PROXY_ENABLED)
  private boolean proxyEnabled;

  @Inject
  @Named(ConfigKeys.PROXY_IP)
  private String proxyIp;

  @Inject
  private ProxyController proxyController;

  @Override
  public boolean shouldModify() {
    return proxyEnabled;
  }

  @Override
  public Capabilities modify(Capabilities capabilities) {
    return enableProxy(capabilities);
  }

  private DesiredCapabilities enableProxy(Capabilities capabilities) {
    DesiredCapabilities caps = new DesiredCapabilities(capabilities);
    try {
      InetAddress proxyInetAddress = InetAddress.getByName(proxyIp);
      BrowserMobProxy browserMobProxy = proxyController.startProxyServer(proxyInetAddress);
      Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy, proxyInetAddress);
      caps.setCapability(CapabilityType.PROXY, seleniumProxy);
    } catch (UnknownHostException e) {
      throw new IllegalStateException(e);
    }
    return caps;
  }
}
```

and related bindings in Guice module:

```java
    Multibinder<CapabilitiesModifier> capabilitiesModifiers = Multibinder
        .newSetBinder(binder(), CapabilitiesModifier.class);
    capabilitiesModifiers.addBinding().to(EnableProxy.class);
```


### Modifying WebDriver or adding additional behavior during its creation
If you have a need to modify an instance of WebDriver or add additional code during its creation, you can do so using yet another extension point in the form of `WebDriverModifier` - just like in the above case, you need to implement this interface and add appropriate bindings to your Guice module.

Example `WebDriverModifier` implementation:
```java
public class MaximizeModifier implements WebDriverModifier {

  @Inject
  @Named(ConfigKeys.WEBDRIVER_MAXIMIZE)
  private boolean maximize;

  @Override
  public boolean shouldModify() {
    return maximize;
  }

  @Override
  public WebDriver modify(WebDriver webDriver) {
    webDriver.manage().window().maximize();
    return webDriver;
  }
}
```

and related bindings in Guice module:
```java
    Multibinder<WebDriverModifier> webDriverModifiers = Multibinder
        .newSetBinder(binder(), WebDriverModifier.class);
    webDriverModifiers.addBinding().to(MaximizeModifier.class);
```

## Custom WebDriver creators

There might be a case where you want to configure a driver in a totally different way or simply don't like how we do it - that is totally fine!

Since 2.2.0, Bobcat offers an additional extension point that allows you to write how you build different drivers.

To do that, all you need to do is implement a `WebDriverCreator` interface and register it in your Guice module. 

Example `WebDriverCreator` implementation:

```java
public class CustomChromeCreator implements WebDriverCreator {

  @Override
  public WebDriver create(Capabilities capabilities) {
    //additional code you might to run here
    return new ChromeDriver(getOptions().merge(capabilities));
  }

  @Override
  public String getId() {
    return "customChrome";
  }

  private ChromeOptions getOptions() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("start-maximized");
    chromeOptions.addArguments("disable-extensions");
    chromeOptions.setExperimentalOption("useAutomationExtension", "false");
    return chromeOptions;
  }
}
```

Bindings in a Guice module:

```java
public class GuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    //...
    
    Multibinder<WebDriverCreator> creators = Multibinder.newSetBinder(binder(), WebDriverCreator.class);

    creators.addBinding().to(CustomChromeCreator.class);
    
    //...
  }
}
```

And now, to use our new creator, we can select it with the `webdriver.type` property:

```bash
./gradlew clean test -Dwebdriver.type=customChrome
```