package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.CreateProjectRequestDto;
import com.vermeulenkylian.backend.DTO.ProjectResponseDto;
import com.vermeulenkylian.backend.DTO.UpdateProjectRequestDto;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.ProjectService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping
    public ProjectResponseDto createProject(@AuthenticationPrincipal User user, @RequestBody CreateProjectRequestDto dto) {
       return projectService.createProject(user,dto);
    }

    @PutMapping("/{id}")
    public ProjectResponseDto  updateProject(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody UpdateProjectRequestDto dto) {
        return projectService.updateProject(user,id,dto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProject(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return projectService.deleteProject(user,id);
    }
}
