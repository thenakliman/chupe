package org.thenakliman.chupe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* NOTE(thenakliman): As this class does not have any other method except
   static method therefore it has been made final, once other methods are
   added, it can be removed
 */
@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}