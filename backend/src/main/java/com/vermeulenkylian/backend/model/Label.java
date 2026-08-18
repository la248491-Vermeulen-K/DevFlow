package com.vermeulenkylian.backend.model;

import jakarta.persistence.*;

@Entity
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String color;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Label(){}

    public Label(Long id, String name, String color, Project project) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
