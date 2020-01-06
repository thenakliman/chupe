package org.thenakliman.chupe.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "authorities")
@Table
public class Role implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "authority", nullable = false)
  private String role;

  public Role(String username, String role) {
    this.username = username;
    this.role = role;
  }
}