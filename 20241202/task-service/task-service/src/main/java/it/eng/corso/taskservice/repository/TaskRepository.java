package it.eng.corso.taskservice.repository;

import it.eng.corso.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitle(String title);

    @Query("select t from Task t where t.completed= ?1")
    List<Task> findCompleted(Boolean param);

    void deleteById(Long id);
}
