package com.vermeulenkylian.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Project {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
