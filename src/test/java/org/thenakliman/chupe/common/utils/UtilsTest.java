package org.thenakliman.chupe.common.utils;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {
  @Test
  public void emptyListIfNull_returnEmptyList_whenNull() {
    assertThat(Utils.emptyListIfNull(null), hasSize(0));
  }

  @Test
  public void emptyListIfNull_returnOriginalList_whenNonNull() {
    assertThat(Utils.emptyListIfNull(Collections.singletonList(12L)), hasSize(1));
  }
}