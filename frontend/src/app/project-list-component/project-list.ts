import { Component, OnInit, signal, inject } from '@angular/core';
import { ProjectService } from '../project-service';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from "@angular/router";
import { ProjectResponse } from '../models/project-response';
import { ProjectDetailComponent } from "../project-detail-component/project-detail-component";

@Component({
  selector: 'app-project-list',
  imports: [DatePipe, ProjectDetailComponent],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectList implements OnInit {
  projects = signal<any[]>([]);
  selectedProject = signal<ProjectResponse | null>(null);

  private projectService = inject(ProjectService);

  constructor(private router: Router){}

  ngOnInit(): void {
    this.projectService.getProjects().subscribe({
      next: (data: any) => {
        this.projects.set(data);
      }
    });
  }

  createProject(){
    this.router.navigate(["/projects/new"])
  }

  openProject(project: ProjectResponse){
    this.selectedProject.set(project);
  }
  
  closeTask(): void {
    this.selectedProject.set(null);
  }
}