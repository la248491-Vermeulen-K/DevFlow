package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.enums.ProjectRole;

import java.time.LocalDateTime;

public class ProjectMemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private ProjectRole role;
    private String avatarUrl;
    private LocalDateTime joinedAt;

    public ProjectMemberResponseDto() {}

    public ProjectMemberResponseDto(Long id, String name, String email, ProjectRole role, String avatarUrl, LocalDateTime joinedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.joinedAt = joinedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
