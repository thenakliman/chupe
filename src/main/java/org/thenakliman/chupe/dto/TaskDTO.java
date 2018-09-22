package org.thenakliman.chupe.dto;

import java.util.Date;

import org.thenakliman.chupe.models.TaskState;


public class TaskDTO {
  private long id;
  private String description;
  private TaskState state;
  private int progress;
  private String createdBy;
  private Date startedOn;
  private Date endedOn;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public TaskState getState() {
    return state;
  }

  public void setState(TaskState state) {
    this.state = state;
  }

  public Date getStartedOn() {
    return startedOn;
  }

  public void setStartedOn(Date startedOn) {
    this.startedOn = startedOn;
  }

  public Date getEndedOn() {
    return endedOn;
  }

  public void setEndedOn(Date endedOn) {
    this.endedOn = endedOn;
  }
}
