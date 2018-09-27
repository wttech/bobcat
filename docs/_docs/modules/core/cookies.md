---
title: "Cookies"
---

Available in `bb-core` since version `1.6.0`
{: .notice--info}

## Overview
You might run into situation where you need to provide a cookie(s) for particular domain(s) before your tests start, e.g. when setting up a cookie to bypass a firewall.

Bobcat provides a mechanism that allows that.

## Defining your cookies

The cookies definitions should be kept under `/cookies` folder under `src/test/resources/`.
By default, Bobcat will look for `cookies.yaml` file.

You can create and manage multiple files (e.g. for different domains) and switch between them by setting the `cookies.file` System property.
{: .notice--info}

The file should have the following structure:

```yaml
cookies:
  - name: 'Example cookie'
    value: 'Bobcat is awesome'
    path: '/'
    domain: 'cognifide.github.io'
    expiry: '2020-09-12 12:12:12'
    secure: true
    httpOnly: true
  - name: 'Example cookie'
    value: 'Example value'
    path: '/'
    domain: 'example.com'
    expiry: '2020-09-12 12:12:12'
    secure: false
    httpOnly: false
  # ... 
```

## How it works

Bobcat will check if the file is present and then load all the cookies and set them after `WebDriver` is created.

Then, it will iterate over the whole list, open each domain (it takes the domain in the cookie and applies `http://` to it) and set the cookie for given domain.  

## Controlling the mechanism

If you have a `cookies.yaml` resource available in your resources, Bobcat will automatically pick it up and apply all the cookies defined inside.
In the case of the file not being there, the mechanism is not triggered.

If for some reason you would like to not set cookies even in the presence of the file, you can suppress the default behaviour by setting the `cookies.loadAutomatically` System property to `false`.

## Extending the mechanism
Requires basic understanding of Guice.
{: .notice--warning}

If you would like to provide another way of loading the cookies, you can create your own provider, by implementing the `Provider<List<CookieData>` interface.

Then, you need to provide proper bindings, replacing the ones in `CookiesModule` that comes out-of-the-box with Bobcat. 