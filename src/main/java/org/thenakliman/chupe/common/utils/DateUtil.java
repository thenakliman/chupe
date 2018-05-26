package org.thenakliman.chupe.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
  public Date getTime() {
    return Calendar.getInstance().getTime();
  }

  /** Add minutes to the given time.
  *
  * @param date Date to which minutes has to be added
  * @param minutes minutes to be added
  * @return
  */
  public Date addMinutes(Date date, int minutes) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MINUTE, minutes);
    return calendar.getTime();
  }
}
