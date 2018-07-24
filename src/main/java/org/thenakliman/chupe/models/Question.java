package org.thenakliman.chupe.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name = "questions")
@Table
public class Question {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "question", nullable = false)
  private String question;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "owner", nullable = false)
  private String owner;

  @Column(name = "assigned_to", nullable = false)
  private String assignedTo;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private QuestionStatus status = QuestionStatus.OPEN;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", nullable = false)
  private QuestionPriority priority;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public QuestionStatus getStatus() {
    return status;
  }

  public void setStatus(QuestionStatus status) {
    this.status = status;
  }

  public QuestionPriority getPriority() {
    return priority;
  }

  public void setPriority(QuestionPriority priority) {
    this.priority = priority;
  }
}
