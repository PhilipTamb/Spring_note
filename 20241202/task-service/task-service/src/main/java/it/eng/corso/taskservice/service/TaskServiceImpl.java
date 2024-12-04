package it.eng.corso.taskservice.service;

import it.eng.corso.taskservice.exception.NoDataFoundException;
import it.eng.corso.taskservice.model.Task;
import it.eng.corso.taskservice.repository.TaskRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Task save(Task task) {
        cacheManager.getCache("tasks").evict(SimpleKey.EMPTY);
        return taskRepository.save(task);
    }

    @Override
    @Cacheable("tasks")
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    @Override
    @Cacheable("tasks")
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
               .orElseThrow(NoDataFoundException::new);
    }

    @Override
    public List<Task> findCompleted(Boolean param) {
        return taskRepository.findCompleted(param);
    }

    @Override
    @CacheEvict("tasks")
    public void deleteById(Long id) {
        cacheManager.getCache("tasks").evict(SimpleKey.EMPTY);
        taskRepository.deleteById(id);
    }

    @Override
    public void updateTask(Long id, String title, Boolean completed, String description) {
        Task t = new Task();
        t.setId(id);
        t.setCompleted(completed);
        t.setTitle(title);
        t.setDescription(description);
        taskRepository.save(t);
    }

    @Override
    public List<Task> search(Boolean completed, String title) {
        return null;
    }
}
