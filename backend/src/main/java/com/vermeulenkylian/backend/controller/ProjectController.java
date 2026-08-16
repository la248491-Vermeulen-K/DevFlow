package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.*;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.model.ProjectMember;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.ProjectMemberService;
import com.vermeulenkylian.backend.service.ProjectService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    public ProjectController(ProjectService projectService, ProjectMemberService projectMemberService) {
        this.projectService = projectService;
        this.projectMemberService = projectMemberService;
    }

    @GetMapping
    public List<ProjectSummaryDto> getProjects(@AuthenticationPrincipal User user) {
        return projectService.getProjects(user);
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

    @GetMapping("/{id}/members")
    public List<ProjectMemberResponseDto> getProjectMembers(@AuthenticationPrincipal User requester, @PathVariable Long id) {
        return projectMemberService.getProjectMembers(requester,id);
    }

    @DeleteMapping("/{id}/members/{userId}")
    public boolean removeMember(@AuthenticationPrincipal User requester, @PathVariable Long id, @PathVariable Long userId) {
        return projectMemberService.removeMember(requester,id,userId);
    }

    @PostMapping("/{id}/members")
    public ProjectMemberResponseDto addMember(@AuthenticationPrincipal User requester, @PathVariable Long id, @RequestBody AddMemberRequestDto dto) {
        return projectMemberService.addMember(requester,id,dto);
    }
}
