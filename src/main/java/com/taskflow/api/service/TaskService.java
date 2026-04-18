package com.taskflow.api.service;

import com.taskflow.api.model.Task;
import com.taskflow.api.model.TaskStatus;
import com.taskflow.api.repository.TaskRepository;
import com.taskflow.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> getTasksByProject(String projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getTasksByAssignee(String userId) {
        return taskRepository.findByAssignId(userId);
    }

    public List<Task> getAllTasksByUser(String userId) {
        return taskRepository.findByAssignId(userId);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task createTask(Task task) {

        if(task.getAssignId() != null & !task.getAssignId().isEmpty()) {
            userRepository.findById(task.getAssignId())
                    .ifPresent(user -> task.setAssigneeName(user.getName()));
        }

        if(task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        return taskRepository.save(task);
    }

    public Task updateTask(String id, Task updatedTask) {
        Task existing = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        existing.setPriority(updatedTask.getPriority());
        existing.setDueDate(updatedTask.getDueDate());

        if(updatedTask.getAssignId() != null) {
            existing.setAssignId(updatedTask.getAssignId());
            userRepository.findById(updatedTask.getAssignId())
                    .ifPresent(user -> existing.setAssigneeName(user.getName()));
        }

        return taskRepository.save(existing);
    }

    public Task updateTaskStatus(String id, TaskStatus newStatus) {
        Task existing = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        existing.setStatus(newStatus);

        return taskRepository.save(existing);
    }

    public void deleteTask(String id) {
        if(!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }

        taskRepository.deleteById(id);
    }

    public Map<String, Long> getTaskStats(String projectId) {
        Map<String, Long> stats = new HashMap<>();

        if(projectId != null && !projectId.isEmpty()) {
            stats.put("TODO", taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.TODO));
            stats.put("IN_PROGRESS", taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.IN_PROGRESS));
            stats.put("DONE", taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.DONE));
        } else {
            stats.put("TODO", taskRepository.countByStatus(TaskStatus.TODO));
            stats.put("IN_PROGRESS", taskRepository.countByStatus(TaskStatus.IN_PROGRESS));
            stats.put("DONE", taskRepository.countByStatus(TaskStatus.DONE));

        }

        stats.put("TOTAL", stats.get("TODO") + stats.get("IN_PROGRESS") + stats.get("DONE"));
        return stats;
    }


}

