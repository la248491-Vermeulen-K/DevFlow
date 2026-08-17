package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.CreateTaskRequestDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.model.Task;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.model.enums.TaskStatus;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import com.vermeulenkylian.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;
    private final PermissionService permissionService;
    private final TaskRepository taskRepository;

    public TaskService(ProjectRepository projectRepository, PermissionService permissionService, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.permissionService = permissionService;
        this.taskRepository = taskRepository;
    }

    public TaskResponseDto createTask(User creator, Long projectId, CreateTaskRequestDto dto){
        if(!permissionService.isMember(creator.getId(), projectId)){
            throw new RuntimeException("Your are not a member of the project");
        }
        Task task = new Task();

        Project project = projectRepository.findById(projectId).orElseThrow(()->new RuntimeException("Project not found"));
        task.setProject(project);
        task.setDescription(dto.getDescription());
        task.setTitle(dto.getTitle());
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.TODO);
        taskRepository.save(task);
        return new TaskResponseDto(task.getId(),task.getDescription(),task.getTitle(),task.getStatus(),task.getCreatedAt());
    }

    public List<TaskResponseDto> getTasks(User user, Long projectId){
        if(!permissionService.isMember(user.getId(), projectId)){
            throw new RuntimeException("You are not a member of this project");
        }
        return taskRepository.findByProjectId(projectId).stream()
                .map(task -> new TaskResponseDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
