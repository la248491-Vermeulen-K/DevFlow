package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.ProjectResponseDto;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectResponseDto> getProjects() {
        return projectService.getProjects().stream()
                .map(project -> new ProjectResponseDto(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}/exists")
    public boolean isExist(@PathVariable Long id) {
        return projectService.existById(id);
    }
}
