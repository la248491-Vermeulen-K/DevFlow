package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
    public boolean existById(Long id) {
        return projectRepository.existsById(id);
    }

}
