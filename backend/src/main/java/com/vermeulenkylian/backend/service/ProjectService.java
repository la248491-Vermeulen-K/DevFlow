package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.model.Project;
import com.vermeulenkylian.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

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

}
