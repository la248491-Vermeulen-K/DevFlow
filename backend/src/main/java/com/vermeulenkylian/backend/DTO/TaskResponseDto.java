package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.Label;
import com.vermeulenkylian.backend.model.enums.TaskPriority;
import com.vermeulenkylian.backend.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private Long assigneeId;
    private String assigneeName;
    private TaskPriority priority;
    private LocalDateTime deadline;
    private Set<LabelResponseDto> labels;

    public TaskResponseDto() {}

    public TaskResponseDto(Long id, String title, String description, TaskStatus status, LocalDateTime createdAt, Long assigneeId, String assigneeName, TaskPriority priority, LocalDateTime deadline,Set<LabelResponseDto> labels) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
        this.priority = priority;
        this.deadline = deadline;
        this.labels = labels;
    }


    public Set<LabelResponseDto> getLabels() {
        return labels;
    }

    public void setLabels(Set<LabelResponseDto> labels) {
        this.labels = labels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
