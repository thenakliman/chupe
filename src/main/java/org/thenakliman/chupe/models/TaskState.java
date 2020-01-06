package org.thenakliman.chupe.models;

import java.util.Arrays;

import static org.thenakliman.chupe.models.TaskStateId.*;

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