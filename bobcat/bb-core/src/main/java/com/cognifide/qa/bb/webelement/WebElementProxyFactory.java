package com.cognifide.qa.bb.webelement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

/**
 * This class can be used to create proxies to web elements.
 * It may be useful when creating {@link com.cognifide.qa.bb.qualifier.PageObject} manually using: {@link com.cognifide.qa.bb.utils.PageObjectInjector} and the element is not yet available on the page.
 * It works in similar way as {@link org.openqa.selenium.support.FindBy} annotation.
 */
public class WebElementProxyFactory {

	/**
	 * Create proxy for WebElement in given search context and selector
	 *
	 * @param searchContext e.g. {@link org.openqa.selenium.WebDriver}
	 * @param selector
	 *
	 * @return proxied WebElement
	 */
	public WebElement proxyForWebElement(SearchContext searchContext, By selector) {
		InvocationHandler handler = new LocatingElementHandler(new SimpleElementLocator(searchContext, selector));

		WebElement proxy;
		proxy = (WebElement) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
		return proxy;
	}

	private class SimpleElementLocator implements ElementLocator {

		private final SearchContext searchContext;
		private final By selector;

		SimpleElementLocator(SearchContext searchContext, By selector) {
			this.searchContext = searchContext;
			this.selector = selector;
		}

		@Override
		public WebElement findElement() {
			return searchContext.findElement(selector);
		}

		@Override
		public List<WebElement> findElements() {
			return searchContext.findElements(selector);
		}
	}
}
