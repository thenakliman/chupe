package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.dto.UpsertTaskDTO;
import org.thenakliman.chupe.services.TaskService;

import javax.validation.Valid;


@Controller
public class TaskController extends BaseController {
  private TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/tasks")
  public ResponseEntity getTasks() {
    return new ResponseEntity<>(taskService.getAllTask(getRequestUsername()), HttpStatus.OK);
  }

  @PostMapping("/tasks")
  public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid UpsertTaskDTO taskDTO) {
    TaskDTO createdTask = taskService.saveTask(taskDTO, getRequestUsername());
    return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<TaskDTO> updateTask(@RequestBody @Valid UpsertTaskDTO taskDTO,
                                            @PathVariable(value = "id") long id) {
    TaskDTO createdTask = taskService.updateTask(id, taskDTO, getRequestUsername());
    return new ResponseEntity<>(createdTask, HttpStatus.OK);
  }
}
