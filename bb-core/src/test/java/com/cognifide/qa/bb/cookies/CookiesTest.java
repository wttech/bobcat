package com.cognifide.qa.bb.cookies;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.cookies.domain.CookieData;

@RunWith(MockitoJUnitRunner.class)
public class CookiesTest {

  @Mock
  private WebDriver webDriver;

  @Mock
  private WebDriver.Options options;

  private List<CookieData> testData = Arrays.asList(
      new CookieData("", "", "", "", null, false, false),
      new CookieData("", "", "", "", null, false, false),
      new CookieData("", "", "", "", null, false, false)
  );

  private Cookies tested = new Cookies(testData);

  @Before
  public void setUp() {
    when(webDriver.manage()).thenReturn(options);
  }

  @Test
  public void shouldSetAllCookiesInWebDriver() {
    tested.setCookies(webDriver);

    verify(webDriver, times(testData.size())).get(any());
    verify(options, times(testData.size())).addCookie(any());
  }
}
