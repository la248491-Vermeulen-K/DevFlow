package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.model.ProjectMember;
import com.vermeulenkylian.backend.model.enums.ProjectRole;
import com.vermeulenkylian.backend.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PermissionService {
    private final ProjectMemberRepository projectMemberRepository;

    public PermissionService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    public Optional<ProjectRole> getRole(Long userId, Long projectId) {
        return projectMemberRepository.findByUserIdAndProjectId(userId, projectId)
                .map(ProjectMember::getRole);
    }

    public boolean isAtLeastAdmin(Long userId, Long projectId) {
        return getRole(userId, projectId)
                .map(role -> role == ProjectRole.ADMIN || role == ProjectRole.OWNER)
                .orElse(false);
    }

    public boolean isOwner(Long userId, Long projectId){
        return getRole(userId,projectId).map(role -> role == ProjectRole.OWNER).orElse(false);
    }

    public boolean isMember(Long userId, Long projectId){
        return getRole(userId, projectId).isPresent();
    }
}
