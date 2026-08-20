package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.model.enums.TaskPriority;
import com.vermeulenkylian.backend.model.enums.TaskStatus;

import java.time.LocalDate;

public class UpdateTaskRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;
    private TaskPriority priority;
    private String assigneeEmail;

    public UpdateTaskRequestDto(){}

    public UpdateTaskRequestDto(String title, String description, TaskStatus status, LocalDate deadline, TaskPriority priority, String assigneeEmail) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.priority = priority;
        this.assigneeEmail = assigneeEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }
}
