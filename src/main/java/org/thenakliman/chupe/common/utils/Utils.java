package org.thenakliman.chupe.common.utils;

import io.jsonwebtoken.lang.Collections;

import java.util.List;

import static java.util.Collections.emptyList;

public class Utils {
  public static <T> List<T> emptyListIfNull(List<T> list) {
    if (Collections.isEmpty(list)) {
      return emptyList();
    }

    return list;
  }
}
