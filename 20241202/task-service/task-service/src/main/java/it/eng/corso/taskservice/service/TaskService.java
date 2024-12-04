package it.eng.corso.taskservice.service;

import it.eng.corso.taskservice.model.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task);

    List<Task> findAll();

    List<Task> findByTitle(String title);
    Task getTaskById(Long id);

    List<Task> findCompleted(Boolean param);

    void deleteById(Long id);
    void updateTask(Long id, String title, Boolean completed, String description);

    List<Task> search(Boolean completed, String title);
}
