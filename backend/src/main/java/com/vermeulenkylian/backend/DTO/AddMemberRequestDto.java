package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.enums.ProjectRole;

public class AddMemberRequestDto {
    private String email;
    private ProjectRole role;

    public AddMemberRequestDto() {}

    public AddMemberRequestDto(String email, ProjectRole role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProjectRole getProjectRole() {
        return role;
    }

    public void setProjectRole(ProjectRole projectRole) {
        this.role = projectRole;
    }
}
