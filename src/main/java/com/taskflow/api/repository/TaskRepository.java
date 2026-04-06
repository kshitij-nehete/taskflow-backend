package com.taskflow.api.repository;

import com.taskflow.api.model.Task;
import com.taskflow.api.model.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByProjectId(String projectId);

    List<Task> findByAssigneeId(String userId); // We might recive error here

    List<Task> findByProjectIdAndStatus(String projectId, TaskStatus status);

    long coundByStatus(TaskStatus status);

    long countByProjectIdAndStatus(String projectId, TaskStatus status);

}
