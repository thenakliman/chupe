package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.TaskService;


@Controller
public class TaskController extends BaseController {
  @Autowired
  private TaskService taskService;

  @GetMapping("/tasks")
  public ResponseEntity getTasks() {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return new ResponseEntity<>(
        taskService.getAllTask(userDetails.getUsername()),
        HttpStatus.OK);
  }

  @PostMapping("/tasks")
  public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    TaskDTO createdTask = taskService.saveTask(taskDTO, userDetails.getUsername());
    return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO,
                                            @PathVariable(value = "id") long id) {
    TaskDTO createdTask = taskService.updateTask(id, taskDTO);
    return new ResponseEntity<>(createdTask, HttpStatus.OK);
  }
}
