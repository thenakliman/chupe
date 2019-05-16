package org.thenakliman.chupe.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.thenakliman.chupe.models.TaskState.CREATED;
import static org.thenakliman.chupe.models.TaskState.DONE;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;
import static org.thenakliman.chupe.models.TaskState.ON_HOLD;

import org.junit.Test;

public class TaskStateIdTest {
  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromCreatedToInProgress() {
    assertTrue(CREATED.transitionPossible(IN_PROGRESS));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromCreatedToCreated() {
    assertTrue(CREATED.transitionPossible(CREATED));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromCreatedToOnHold() {
    assertFalse(CREATED.transitionPossible(ON_HOLD));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromCreatedToDone() {
    assertFalse(CREATED.transitionPossible(DONE));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromInProgressToCreated() {
    assertTrue(IN_PROGRESS.transitionPossible(IN_PROGRESS));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromInProgressToInProgress() {
    assertFalse(IN_PROGRESS.transitionPossible(CREATED));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromInProgressToOnHold() {
    assertTrue(IN_PROGRESS.transitionPossible(ON_HOLD));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromInProgressToDone() {
    assertTrue(IN_PROGRESS.transitionPossible(DONE));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromOnHoldToInProgress() {
    assertTrue(ON_HOLD.transitionPossible(IN_PROGRESS));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromOnHoldToCreated() {
    assertFalse(ON_HOLD.transitionPossible(CREATED));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromOnHoldToOnHold() {
    assertTrue(ON_HOLD.transitionPossible(ON_HOLD));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromOnHoldToDone() {
    assertFalse(ON_HOLD.transitionPossible(DONE));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromDoneToCreated() {
    assertFalse(DONE.transitionPossible(CREATED));
  }

  @Test
  public void transitionPossible_shouldReturnTrue_whenTransitionFromDoneToInProgress() {
    assertTrue(DONE.transitionPossible(IN_PROGRESS));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromDoneToOnHold() {
    assertFalse(DONE.transitionPossible(ON_HOLD));
  }

  @Test
  public void transitionPossible_shouldReturnFalse_whenTransitionFromDoneToDone() {
    assertTrue(DONE.transitionPossible(DONE));
  }
}