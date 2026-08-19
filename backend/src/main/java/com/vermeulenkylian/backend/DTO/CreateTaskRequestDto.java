package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.enums.TaskPriority;

public class CreateTaskRequestDto {
    private String title;
    private String description;
    private TaskPriority priority;

    public CreateTaskRequestDto() {}

    public CreateTaskRequestDto(String title, String description, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
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

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}
