package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.CreateLabelRequestDto;
import com.vermeulenkylian.backend.DTO.LabelResponseDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.exception.BadRequestException;
import com.vermeulenkylian.backend.exception.ForbiddenException;
import com.vermeulenkylian.backend.exception.NotFoundException;
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
            throw new ForbiddenException("You don't have the permission to do that");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        Label label = new Label();
        label.setName(dto.getName());
        label.setColor(dto.getColor());
        label.setProject(project);
        labelRepository.save(label);
        return new LabelResponseDto(label.getId(),label.getName(),label.getColor());
    }

    public List<LabelResponseDto> getLabels(User user, Long projectId) {
        if (!permissionService.isMember(user.getId(), projectId)) {
            throw new ForbiddenException("You are not a member of this project");
        }
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("Project not found");
        }

        return labelRepository.findByProjectId(projectId).stream()
                .map(label -> new LabelResponseDto(label.getId(), label.getName(), label.getColor()))
                .toList();
    }

    public TaskResponseDto addLabelToTask(User user, Long taskId, Long labelId){
        Task task = getTaskAndCheckPermission(user, taskId);
        Label label = getLabelForTask(labelId, task);
        if(task.getLabels().contains(label)){
            throw new BadRequestException("This label is already attached to the task");
        }
        task.getLabels().add(label);
        taskRepository.save(task);
        return toDto(task);
    }

    public TaskResponseDto removeLabelFromTask(User user, Long taskId, Long labelId){
        Task task = getTaskAndCheckPermission(user, taskId);
        Label label = getLabelForTask(labelId, task);
        if(!task.getLabels().contains(label)){
            throw new BadRequestException("This label is not attached to the task");
        }
        task.getLabels().remove(label);
        taskRepository.save(task);
        return toDto(task);
    }

    public boolean deleteLabel(User user, Long labelId){
        Label label = labelRepository.findById(labelId).orElseThrow(() -> new NotFoundException("Label not found"));
        if(!permissionService.isAtLeastAdmin(user.getId(), label.getProject().getId())){
            throw new ForbiddenException("You don't have the permission to do that");
        }
        labelRepository.delete(label);
        return true;
    }

    private Task getTaskAndCheckPermission(User user, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!permissionService.isAtLeastAdmin(user.getId(), task.getProject().getId())) {
            throw new ForbiddenException("You don't have the permission to do that");
        }
        return task;
    }

    private Label getLabelForTask(Long labelId, Task task) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new NotFoundException("Label not found"));

        if (!label.getProject().getId().equals(task.getProject().getId())) {
            throw new BadRequestException("Label does not belong to the task's project");
        }
        return label;
    }
}
