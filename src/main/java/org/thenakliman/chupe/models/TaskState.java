package org.thenakliman.chupe.models;

import static org.thenakliman.chupe.models.TaskStateId.CREATED_ID;
import static org.thenakliman.chupe.models.TaskStateId.DONE_ID;
import static org.thenakliman.chupe.models.TaskStateId.IN_PROGRESS_ID;
import static org.thenakliman.chupe.models.TaskStateId.ON_HOLD_ID;

import java.util.Arrays;

public enum TaskState {
  CREATED(
      CREATED_ID,
      CREATED_ID,
      IN_PROGRESS_ID),

  IN_PROGRESS(
      IN_PROGRESS_ID,
      IN_PROGRESS_ID,
      ON_HOLD_ID,
      DONE_ID),

  ON_HOLD(
      ON_HOLD_ID,
      ON_HOLD_ID,
      IN_PROGRESS_ID),

  DONE(
      DONE_ID,
      DONE_ID,
      IN_PROGRESS_ID);

  TaskStateId[] nextPossibleStates;
  TaskStateId value;

  TaskState(TaskStateId value, TaskStateId... nextPossibleStates) {
    this.nextPossibleStates = nextPossibleStates;
    this.value = value;
  }

  public TaskStateId getValue() {
    return value;
  }

  public boolean transitionPossible(TaskState nextState) {
    return Arrays.stream(nextPossibleStates)
        .anyMatch(nextPossibleState -> nextPossibleState.equals(nextState.getValue()));
  }
}