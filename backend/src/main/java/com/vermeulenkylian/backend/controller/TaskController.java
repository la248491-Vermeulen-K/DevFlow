package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.CreateTaskRequestDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.model.Task;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponseDto createTask(@AuthenticationPrincipal User user, @RequestBody CreateTaskRequestDto dto, @PathVariable Long projectId) {
        return taskService.createTask(user,projectId,dto);
    }

    @GetMapping
    public List<TaskResponseDto> getTasks(@AuthenticationPrincipal User user, @PathVariable Long projectId) {
        return taskService.getTasks(user, projectId);
    }

}
