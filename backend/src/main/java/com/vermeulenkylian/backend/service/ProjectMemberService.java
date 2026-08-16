package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.AddMemberRequestDto;
import com.vermeulenkylian.backend.DTO.ProjectMemberResponseDto;
import com.vermeulenkylian.backend.model.ProjectMember;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.model.enums.ProjectRole;
import com.vermeulenkylian.backend.repository.ProjectMemberRepository;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import com.vermeulenkylian.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectMemberService {


    private final PermissionService permissionService;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    public ProjectMemberService(PermissionService permissionService, UserRepository userRepository, ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository) {
        this.permissionService = permissionService;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
    }

    public boolean addMember(User requester, Long projectId, AddMemberRequestDto dto){
        if (!permissionService.isAtLeastAdmin(requester.getId(), projectId)){
            throw new RuntimeException("You don't have permission to add member");
        }
        User newMember = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Member not found"));
        if (projectMemberRepository.findByUserIdAndProjectId(newMember.getId(), projectId).isPresent()){
            throw new RuntimeException("Member already exists");
        }
        ProjectMember member = new ProjectMember();
        member.setUser(newMember);
        member.setProject(projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found")));
        member.setRole(dto.getProjectRole());
        member.setJoinedAt(LocalDateTime.now());
        projectMemberRepository.save(member);
        return true;
    }

    public boolean removeMember(User requester, Long projectId, Long userIdToRemove) {
        if (!permissionService.isAtLeastAdmin(requester.getId(), projectId)) {
            throw new RuntimeException("You don't have permission to remove member");
        }
        ProjectMember projectMember = projectMemberRepository.findByUserIdAndProjectId(userIdToRemove, projectId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (projectMember.getRole() == ProjectRole.OWNER) {
            throw new RuntimeException("Cannot remove the project owner");
        }

        projectMemberRepository.delete(projectMember);
        return true;
    }

    public List<ProjectMemberResponseDto> getProjectMembers(User requester, Long projectId) {
        if(projectRepository.findById(projectId).isEmpty()){
            throw new RuntimeException("Project not found");
        }
        if (!permissionService.isMember(requester.getId(), projectId)) {
            throw new RuntimeException("You are not a member of this project");
        }
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(member -> new ProjectMemberResponseDto(
                        member.getUser().getId(),
                        member.getUser().getName(),
                        member.getUser().getEmail(),
                        member.getRole(),
                        member.getUser().getAvatarUrl(),
                        member.getJoinedAt()
                ))
                .collect(Collectors.toList());
    }
}
