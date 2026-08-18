package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.AssignTaskRequestDto;
import com.vermeulenkylian.backend.DTO.CreateTaskRequestDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.DTO.UpdateTaskStatusRequestDto;
import com.vermeulenkylian.backend.model.Task;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.LabelService;
import com.vermeulenkylian.backend.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final LabelService labelService;

    public TaskController(TaskService taskService, LabelService labelService) {
        this.taskService = taskService;
        this.labelService = labelService;
    }

    @PatchMapping("/{id}/status")
    public TaskResponseDto updateTaskStatus(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody UpdateTaskStatusRequestDto dto) {
        return taskService.updateStatus(user, id, dto.getNewStatus());
    }

    @PatchMapping("/{id}/assign")
    public TaskResponseDto assignTask(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody AssignTaskRequestDto dto) {
        return taskService.assignTask(user, id, dto);
    }
    @PatchMapping("/{taskId}/labels/{labelId}")
    public TaskResponseDto addLabel(@AuthenticationPrincipal User user, @PathVariable Long taskId, @PathVariable Long labelId) {
        return labelService.addLabelToTask(user, taskId, labelId);
    }

    @DeleteMapping("/{taskId}/labels/{labelId}")
    public TaskResponseDto removeLabel(@AuthenticationPrincipal User user, @PathVariable Long taskId, @PathVariable Long labelId) {
        return labelService.removeLabelFromTask(user, taskId, labelId);
    }

}
