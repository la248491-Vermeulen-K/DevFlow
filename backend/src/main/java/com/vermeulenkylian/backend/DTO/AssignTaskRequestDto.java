package com.vermeulenkylian.backend.DTO;

public class AssignTaskRequestDto {
    private String email;

    public AssignTaskRequestDto(String email) {
        this.email = email;
    }

    public AssignTaskRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
