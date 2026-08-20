package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.CreateProjectRequestDto;
import com.vermeulenkylian.backend.DTO.ProjectResponseDto;
import com.vermeulenkylian.backend.DTO.ProjectSummaryDto;
import com.vermeulenkylian.backend.DTO.UpdateProjectRequestDto;
import com.vermeulenkylian.backend.exception.ForbiddenException;
import com.vermeulenkylian.backend.exception.NotFoundException;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.model.ProjectMember;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.model.enums.ProjectRole;
import com.vermeulenkylian.backend.repository.ProjectMemberRepository;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final PermissionService permissionService;

    public ProjectService(ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository, PermissionService permissionService) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.permissionService = permissionService;
    }
    public List<ProjectSummaryDto> getProjects(User user) {
        List<ProjectSummaryDto> summaries = new ArrayList<>();
        List<ProjectMember> projectMemberList = projectMemberRepository.findByUserId(user.getId());
        for (ProjectMember projectMember : projectMemberList) {
            Project project =projectMember.getProject();
            ProjectSummaryDto projectSummaryDto = new ProjectSummaryDto(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getCreatedAt(),
                    projectMember.getRole(),
                    projectMemberRepository.countByProjectId(project.getId())
            );
            summaries.add(projectSummaryDto);
        }
        return summaries;
    }
    public boolean existById(Long id) {
        return projectRepository.existsById(id);
    }

    public ProjectResponseDto createProject(User creator, CreateProjectRequestDto dto){
        Project project = new Project();
        ProjectMember projectMember = new ProjectMember();
        project.setName(dto.getName());
        project.setCreatedAt(LocalDateTime.now());
        project.setDescription(dto.getDescription());
        projectRepository.save(project);
        projectMember.setProject(project);
        projectMember.setUser(creator);
        projectMember.setRole(ProjectRole.OWNER);
        projectMember.setJoinedAt(LocalDateTime.now());
        projectMemberRepository.save(projectMember);
        return new ProjectResponseDto(project.getId(), project.getName(), project.getDescription(), project.getCreatedAt());
    }

    public ProjectResponseDto updateProject(User user, Long projectId, UpdateProjectRequestDto dto){
        if(!permissionService.isAtLeastAdmin(user.getId(), projectId)){
            throw new ForbiddenException("You don't have the permission to do that");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        projectRepository.save(project);
        return new ProjectResponseDto(project.getId(), project.getName(), project.getDescription(), project.getCreatedAt());
    }

    @Transactional
    public boolean deleteProject(User user, Long projectId){
        if(!permissionService.isOwner(user.getId(), projectId)){
            throw new ForbiddenException("You don't have the permission to do that");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        projectMemberRepository.deleteByProjectId(projectId);
        projectRepository.delete(project);
        return true;
    }

}
