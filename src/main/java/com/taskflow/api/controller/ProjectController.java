package com.taskflow.api.controller;

import com.taskflow.api.dto.ApiResponse;
import com.taskflow.api.model.Project;
import com.taskflow.api.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects(HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");
        List<Project> projects = projectService.getProjectsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Projects loaded", projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> getProject(@PathVariable String id) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(ApiResponse.success("Project found", project));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Project>> createProject(@RequestBody Project project, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            Project created = projectService.createProject(project, userId);
            return ResponseEntity.ok(ApiResponse.success("Project created", created));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> updateProject(@PathVariable String id, @RequestBody Project project, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            Project updated = projectService.updateProject(id, project, userId);
            return ResponseEntity.ok(ApiResponse.success("Project updated", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable String id, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            projectService.deleteProject(id, userId);
            return ResponseEntity.ok(ApiResponse.success("Project deletedd", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
