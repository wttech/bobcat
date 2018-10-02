package com.cognifide.qa.bb.wait;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

@RunWith(MockitoJUnitRunner.class)
public class BobcatWaitTest {

  @Mock
  private WebDriver webDriver;

  @Mock
  private WebDriver.Options options;

  @Mock
  private WebDriver.Timeouts timeouts;

  private BobcatWait tested;

  @Before
  public void setup() {
    when(webDriver.manage()).thenReturn(options);
    when(options.timeouts()).thenReturn(timeouts);
    when(timeouts.implicitlyWait(anyLong(), any())).thenReturn(timeouts);
    tested = new BobcatWait(webDriver);
  }

  @Test
  public void whenUntilIsInvokedImplicitTimeoutsShouldBeReducedThenRestored() {
    BobcatWait spied = spy(tested);
    InOrder inOrder = inOrder(spied);
    when(condition.apply(any())).thenReturn(true);

    spied.until(condition);

    inOrder.verify(spied).setImplicitTimeoutToNearZero();
    inOrder.verify(spied).getWebDriverWait();
    inOrder.verify(spied).restoreImplicitTimeout();
  }

  @Mock
  private ExpectedCondition<Boolean> condition;

  @Test
  public void isConditionMetShouldCatchTimeoutExceptionAndReturnBoolean() {
    when(condition.apply(any())).thenThrow(new TimeoutException());

    boolean result = true;
    try {
      result = tested.isConditionMet(condition);
    } catch (TimeoutException e) {
      fail("Exception should not be thrown");
    }
    assertThat(result).isFalse();
  }
}
