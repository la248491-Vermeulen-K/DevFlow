import { Component, OnInit, signal, inject } from '@angular/core';
import { ProjectService } from '../project-service';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from "@angular/router";

@Component({
  selector: 'app-project-list',
  imports: [DatePipe],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectList implements OnInit {
  projects = signal<any[]>([]);

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

  openProject(projectId: number){
    this.router.navigate([`/projects/${projectId}`])
  }
}