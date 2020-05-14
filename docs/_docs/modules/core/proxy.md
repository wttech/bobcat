---
title: "Working with proxy"
---

## Overview
Potentially, you might run into a case, when you want to modify HTTP requests during your test runs.
Selenium does not have an in-built solution for that, but provides an option to hook up a proxy to 
the browser it drives.

Bobcat uses `BrowserMobProxy` as a proxy solution. It can be controlled programmatically from your tests.

### Enabling proxy
To enable proxy use dedicated properties in your config.

Default config:
```yaml
    proxy.enabled: false
    proxy.ip: 127.0.0.1
    proxy.port: 9000
```

- `proxy.enabled` - enable the proxy
- `proxy.ip` - define the IP on which the proxy listens (useful for external proxy)
- `proxy.port` - define the port used by the proxy

### Modifying requests
Most common use case when working with proxy is modifying requests, to e.g. attach additional header.
This can be done by registering a `RequestFilter` in the `RequestFilterRegistry`.

Example:
```java
@Inject
private RequestFilterRegistry filters;

//...

void setHeader() {
    filters.add((request, contents, messageInfo) -> {
      request.headers().add("headerName", "headerValue");
      return null;
    });
}
```
