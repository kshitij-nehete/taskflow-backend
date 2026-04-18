package com.taskflow.api.controller;

import com.taskflow.api.dto.ApiResponse;
import com.taskflow.api.model.Task;
import com.taskflow.api.model.TaskStatus;
import com.taskflow.api.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getTasks(
            @RequestParam(required = false) String projectId, HttpServletRequest request
    ) {
        String userId = (String) request.getAttribute(("userId"));
        List<Task> tasks;

        if(projectId != null && !projectId.isEmpty()) {
            tasks = taskService.getTasksByProject(projectId);
        } else {
            tasks = taskService.getAllTasksByUser(userId);
        }

        return ResponseEntity.ok(ApiResponse.success("Task loaded", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getTask(@PathVariable String id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(ApiResponse.success("Task found", task));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@RequestBody Task task, HttpServletRequest request) {

        try {
            String userId = (String) request.getAttribute("userId");

            if(task.getAssignId() == null || task.getAssignId().isEmpty()) {
                task.setAssignId(userId);
            }

            Task created = taskService.createTask(task);

            return ResponseEntity.ok(ApiResponse.success("Task created", created));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity <ApiResponse<Task>> updateTask(@PathVariable String id, @RequestBody Task task) {
        try {
            Task updated = taskService.updateTask(id, task);

            return ResponseEntity.ok(ApiResponse.success("Task updated", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity <ApiResponse<Task>> updateStatus(
            @PathVariable String id, @RequestParam TaskStatus status
            ) {
        try {
            Task updated = taskService.updateTaskStatus(id, status);


            return ResponseEntity.ok(ApiResponse.success("Status updated", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {

        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(ApiResponse.success("Task deleted", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats(
            @RequestParam(required = false) String projectId
    ) {
        Map<String, Long> stats = taskService.getTaskStats(projectId);
        return ResponseEntity.ok(ApiResponse.success("Stats loaded", stats));
    }
}
