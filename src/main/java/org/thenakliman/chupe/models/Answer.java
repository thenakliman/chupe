package org.thenakliman.chupe.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "answers")
@Table
public class Answer {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "answer", nullable = false)
  private String answer;

  @Column(name = "answered_by", nullable = false)
  private String answeredBy;

  @Column(name = "question_id", nullable = false)
  private long questionId;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;
}
