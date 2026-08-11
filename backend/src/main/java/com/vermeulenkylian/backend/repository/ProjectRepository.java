package com.vermeulenkylian.backend.repository;

import com.vermeulenkylian.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
