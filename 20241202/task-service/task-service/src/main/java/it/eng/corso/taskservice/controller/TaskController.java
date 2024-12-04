package it.eng.corso.taskservice.controller;

import it.eng.corso.taskservice.model.Task;
import it.eng.corso.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/api/v1/taskTitle/{title}")
    public List<Task> findByAuthor(@PathVariable String title) {
        return taskService.findByTitle(title);
    }

    @GetMapping("/{id}")
    public Task findByAuthor(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @GetMapping("/search")
    public List<Task> find(@RequestParam( required = false ) Boolean completed,
                           @RequestParam( required = false ) String title) {//Cambire param
        return taskService.search(completed, title);
    }

    @PostMapping
    public Task saveTask(@RequestBody Task task) {
        return taskService.save(task);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id, @RequestParam(defaultValue = "title") String title, @RequestParam(defaultValue = "false") Boolean completed, @RequestParam(defaultValue = "Descrizione") String description) {
        taskService.updateTask(id, title, completed, description);
    }

}
