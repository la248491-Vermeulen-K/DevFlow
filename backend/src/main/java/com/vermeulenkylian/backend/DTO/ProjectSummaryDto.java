package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.enums.ProjectRole;

import java.time.LocalDateTime;

public class ProjectSummaryDto {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private ProjectRole projectRole;
        private Long numberOfMembers;

        public ProjectSummaryDto() {
        }

        public ProjectSummaryDto(Long id, String name, String description, LocalDateTime createdAt, ProjectRole projectRole, Long numberOfMembers) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createdAt = createdAt;
            this.projectRole = projectRole;
            this.numberOfMembers = numberOfMembers;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description; 
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

    public Long getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(Long numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }
}
