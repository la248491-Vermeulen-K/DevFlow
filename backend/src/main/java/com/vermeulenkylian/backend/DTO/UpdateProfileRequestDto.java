package com.vermeulenkylian.backend.DTO;

public class UpdateProfileRequestDto {
    private String name;
    private String bio;

    public UpdateProfileRequestDto() {
    }

    public UpdateProfileRequestDto(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
