package org.thenakliman.chupe.dto;

public class QuestionDTO {
  private int id;

  private String question;

  private String description;

  private String owner;

  private String assignedTo;

  /** Empty constructor for spring to create bean. */
  public QuestionDTO() {
    /** For spring to create empty object */
  }

  /** Constructor for QuestionDTO.
   *
   * @param id question id
   * @param question question
   * @param description description or detail to support question
   * @param owner user asked the question
   * @param assignedTo user responsible to answer question
   */
  public QuestionDTO(int id, String question, String description, String owner, String assignedTo) {
    this.id = id;
    this.question = question;
    this.description = description;
    this.owner = owner;
    this.assignedTo = assignedTo;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
  }
}
