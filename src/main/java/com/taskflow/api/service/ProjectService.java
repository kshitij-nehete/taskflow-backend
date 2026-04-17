package com.taskflow.api.service;

import com.taskflow.api.model.Project;
import com.taskflow.api.repository.ProjectRepository;
import com.taskflow.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public List<Project> getProjectsByUser(String userId) {
        return projectRepository.findByCreatedBy(userId);
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project createProject(Project project, String userId)  {
        project.setCreatedBy(userId);
        return projectRepository.save(project);
    }

    public Project updateProject(String id, Project updatedProject, String userId) {
        Project existing = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if(!existing.getCreatedBy().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this project");
        }

        existing.setName(updatedProject.getName());
        existing.setDescription(updatedProject.getDescription());
        return projectRepository.save(existing);
    }

    public void deleteProject(String id, String userId) {
        Project existing = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if(!existing.getCreatedBy().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this project");
        }

        taskRepository.findByProjectId(id).forEach(task -> taskRepository.deleteById(task.getId()));

        projectRepository.deleteById(id);
    }
}
