package org.thenakliman.chupe.models;


enum TaskStateId {
  CREATED_ID(0),
  IN_PROGRESS_ID(1),
  ON_HOLD_ID(1),
  DONE_ID(1);
  int value;

  TaskStateId(int value) {
    this.value = value;
  }
}
