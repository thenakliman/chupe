package org.thenakliman.chupe.common.utils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DateUtilTest {

  @Mock
  Calendar calendar;

  @InjectMocks
  DateUtil dateUtil;

  // TODO(thenakliman): Write test for date util.
  @Ignore
  @Test
  public void shouldReturnCurrentTime() {
    Date currentTime = new Date();
    BDDMockito.given(calendar.getTime()).willReturn(currentTime);
    BDDMockito.given(calendar.getInstance()).willReturn(calendar);
    Date time = dateUtil.getTime();
    assertEquals(currentTime, time);
  }

}