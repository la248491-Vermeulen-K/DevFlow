package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.AssignTaskRequestDto;
import com.vermeulenkylian.backend.DTO.CreateTaskRequestDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.mapper.TaskMapper;
import com.vermeulenkylian.backend.model.Label;
import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.model.Task;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.model.enums.TaskPriority;
import com.vermeulenkylian.backend.model.enums.TaskStatus;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import com.vermeulenkylian.backend.repository.TaskRepository;
import com.vermeulenkylian.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vermeulenkylian.backend.mapper.TaskMapper.toDto;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;
    private final PermissionService permissionService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(ProjectRepository projectRepository, PermissionService permissionService, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.permissionService = permissionService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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
        task.setPriority(dto.getPriority());
        taskRepository.save(task);
        return toDto(task);
    }

    public List<TaskResponseDto> getTasks(User user, Long projectId){
        if(!permissionService.isMember(user.getId(), projectId)){
            throw new RuntimeException("You are not a member of this project");
        }
        return taskRepository.findByProjectId(projectId).stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskResponseDto updateStatus(User user, Long taskId, TaskStatus newStatus){
        Task task = taskRepository.findById(taskId).orElseThrow(()->new RuntimeException("Task not found"));
        if(!permissionService.isMember(user.getId(),task.getProject().getId())){
            throw new RuntimeException("You are not a member of the project");
        }
        task.setStatus(newStatus);
        taskRepository.save(task);
        return toDto(task);
    }

    public TaskResponseDto assignTask(User user, Long taskId, AssignTaskRequestDto dto){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if(!permissionService.isMember(user.getId(), task.getProject().getId())){
            throw new RuntimeException("You are not a member of the project");
        }

        User assignee = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if(!permissionService.isMember(assignee.getId(), task.getProject().getId())){
            throw new RuntimeException("This user is not a member of the project and cannot be assigned");
        }

        task.setAssignee(assignee);
        taskRepository.save(task);
        return toDto(task);
    }

    public boolean deleteTask(User user,Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!permissionService.isAtLeastAdmin(user.getId(), task.getProject().getId())){
            throw new RuntimeException("You don't have the permission to do that");
        }
        taskRepository.delete(task);
        return true;
    }
}
