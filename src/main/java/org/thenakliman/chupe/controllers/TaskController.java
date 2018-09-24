package org.thenakliman.chupe.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
  public ResponseEntity getTasks() {
    try {
      return new ResponseEntity<>(taskService.getAllTask(), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }

  /** API for creating task.
   *
   */
  @PostMapping("/tasks")
  public ResponseEntity<TaskDTO> createTask(@RequestHeader HttpHeaders header,
                                                 @RequestBody TaskDTO taskDTO) {

    TaskDTO createdTask = taskService.saveTask(taskDTO);
    return new ResponseEntity<>(createdTask, HttpStatus.OK);
  }

  /** API for creating task.
   *
   */
  @PutMapping("/tasks/{id}")
  public ResponseEntity<TaskDTO> updateTask(@RequestHeader HttpHeaders header,
                                            @RequestBody TaskDTO taskDTO,
                                            @PathVariable(value = "id") long id) {
    TaskDTO createdTask;

    try {
      createdTask = taskService.updateTask(id, taskDTO);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(createdTask, HttpStatus.OK);
  }
}
