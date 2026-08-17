package com.vermeulenkylian.backend.DTO;

import com.vermeulenkylian.backend.model.enums.TaskStatus;

public class UpdateTaskStatusRequestDto {
    private TaskStatus newStatus;

    public UpdateTaskStatusRequestDto(TaskStatus newStatus) {
        this.newStatus = newStatus;
    }

    public UpdateTaskStatusRequestDto() {
    }

    public TaskStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(TaskStatus newStatus) {
        this.newStatus = newStatus;
    }
}
