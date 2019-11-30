package org.thenakliman.chupe.common.utils;

import static java.util.Collections.emptyList;

import java.util.List;

import io.jsonwebtoken.lang.Collections;

public class Utils {
  public static <T> List<T> emptyListIfNull(List<T> list) {
    if (Collections.isEmpty(list)) {
      return emptyList();
    }

    return list;
  }
}
