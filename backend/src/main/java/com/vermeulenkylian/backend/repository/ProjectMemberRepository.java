package com.vermeulenkylian.backend.repository;

import com.vermeulenkylian.backend.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByUserIdAndProjectId(Long userId, Long projectId);
    void deleteByProjectId(Long projectId);
    List<ProjectMember> findByProjectId(Long projectId);
}