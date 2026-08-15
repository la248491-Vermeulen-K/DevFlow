package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.enums.ProjectRole;

public class AddMemberRequestDto {
    private String email;
    private ProjectRole projectRole;

    public AddMemberRequestDto() {}

    public AddMemberRequestDto(String email, ProjectRole projectRole) {
        this.email = email;
        this.projectRole = projectRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }
}
