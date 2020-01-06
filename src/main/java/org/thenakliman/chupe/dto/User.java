package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

  private String username;

  private String email;

  private String name;

  private List<String> roles;
}
