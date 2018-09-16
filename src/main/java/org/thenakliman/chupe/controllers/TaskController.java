package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.services.TaskService;


@Controller
public class TaskController extends BaseController {
  @Autowired
  private TaskService taskService;

  /** Returns list of tasks.
   *
   * @return list of Tasks
   */
  @GetMapping("/tasks")
  public ResponseEntity<List<TaskDTO>> getTasks() {
    try {
      return new ResponseEntity<>(taskService.getAllTask(), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
