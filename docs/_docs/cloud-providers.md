---
title: Cloud providers
---

This page describes how to integrate with specific cloud services using Bobcat.

## BrowserStack

If you have a BrowserStack account, you can use it as your source of test infrastructure resources. 

Having an account you should have a username and an access key at your disposal. You should not store them in your version control system! It's better to pass them from your CI server as an encrypted environmental variables.

### Configuration
Like with all cloud services, to use it you must set correct WebDriver type and provider's Grid URL, proper browser and OS version.

To do this in Bobcat, add the browser's capabilities (either in your config files or via command-line arguments).

Example context for Chrome browser on Windows 10:
```yaml
browserstack-chrome:
  webdriver.type: remote
  webdriver.cap.browser: Chrome
  webdriver.cap.browser_version: 62
  webdriver.cap.os: Windows
  webdriver.cap.os_version: 10
```

For more information and additional capabilities configuration, please visit [the official BrowserStack documentation](https://www.browserstack.com/automate/java#configure-capabilities)

After setting the above and storing BrowserStack credentials securely (and, in this example, exposing them as `BROWSERSTACK_USERNAME` and `BROWSERSTACK_ACCESS_KEY` environmental variables) run the tests with following command-line argument:
```
-Dwebdriver.url=https://${BROWSERSTACK_USERNAME}:${BROWSERSTACK_ACCESS_KEY}@hub-cloud.browserstack.com/wd/hub
```

In case of Gradle:
```
./gradlew clean test -Dwebdriver.url=https://${BROWSERSTACK_USERNAME}:${BROWSERSTACK_ACCESS_KEY}@hub-cloud.browserstack.com/wd/hub
```

You can check out example configuration of BrowserStack in Bobcat's test suites. Tests are run on Travis: [build](https://travis-ci.org/Cognifide/bobcat), [config](https://github.com/Cognifide/bobcat/blob/master/.travis.yml).
{: .notice--info}