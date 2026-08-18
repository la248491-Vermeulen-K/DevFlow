package com.vermeulenkylian.backend.mapper;

import com.vermeulenkylian.backend.DTO.LabelResponseDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.model.Label;
import com.vermeulenkylian.backend.model.Task;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskMapper {
    public static TaskResponseDto toDto(Task task) {
        Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
        String assigneeName = task.getAssignee() != null ? task.getAssignee().getName() : null;
        Set<LabelResponseDto> labelDtos = task.getLabels() != null
                ? task.getLabels().stream()
                  .map(label -> new LabelResponseDto(label.getId(), label.getName(), label.getColor()))
                  .collect(Collectors.toSet())
                : new HashSet<>();

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                assigneeId,
                assigneeName,
                task.getPriority(),
                task.getDeadline(),
                labelDtos
        );
    }
}
