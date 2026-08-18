package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.CreateLabelRequestDto;
import com.vermeulenkylian.backend.DTO.LabelResponseDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.model.Label;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.model.Task;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.LabelRepository;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import com.vermeulenkylian.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.vermeulenkylian.backend.mapper.TaskMapper.toDto;

@Service
public class LabelService {
    private final PermissionService permissionService;
    private final ProjectRepository projectRepository;
    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;

    public LabelService(PermissionService permissionService, ProjectRepository projectRepository, LabelRepository labelRepository, TaskRepository taskRepository) {
        this.permissionService = permissionService;
        this.projectRepository = projectRepository;
        this.labelRepository = labelRepository;
        this.taskRepository = taskRepository;
    }

    public LabelResponseDto createLabel(User user, Long projectId, CreateLabelRequestDto dto){
        if(!permissionService.isAtLeastAdmin(user.getId(), projectId)){
            throw new RuntimeException("You don't have the permission to do that");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Label label = new Label();
        label.setName(dto.getName());
        label.setColor(dto.getColor());
        label.setProject(project);
        labelRepository.save(label);
        return new LabelResponseDto(label.getId(),label.getName(),label.getColor());
    }

    public TaskResponseDto addLabelToTask(User user, Long taskId, Long labelId){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if(!permissionService.isAtLeastAdmin(user.getId(), task.getProject().getId())){
            throw new RuntimeException("You don't have the permission to do that");
        }
        Label label = labelRepository.findById(labelId).orElseThrow(() -> new RuntimeException("Label not found"));
        if(!label.getProject().getId().equals(task.getProject().getId())){
            throw new RuntimeException("This label does not belong to the task's project");
        }
        if(task.getLabels().contains(label)){
            throw new RuntimeException("This label is already attached to the task");
        }
        task.getLabels().add(label);
        taskRepository.save(task);
        return toDto(task);
    }

    public TaskResponseDto removeLabelFromTask(User user, Long taskId, Long labelId){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if(!permissionService.isAtLeastAdmin(user.getId(), task.getProject().getId())){
            throw new RuntimeException("You don't have the permission to do that");
        }
        Label label = labelRepository.findById(labelId).orElseThrow(() -> new RuntimeException("Label not found"));
        if(!label.getProject().getId().equals(task.getProject().getId())){
            throw new RuntimeException("This label does not belong to the task's project");
        }
        if(!task.getLabels().contains(label)){
            throw new RuntimeException("This label is not attached to the task");
        }
        task.getLabels().remove(label);
        taskRepository.save(task);
        return toDto(task);
    }
}
