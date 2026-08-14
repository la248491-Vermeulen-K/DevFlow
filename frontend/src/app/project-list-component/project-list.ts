import { Component, OnInit, signal, inject } from '@angular/core';
import { ProjectService } from '../project-service';

@Component({
  selector: 'app-project-list',
  imports: [],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectList implements OnInit {
  projects = signal<any[]>([]);

  private projectService = inject(ProjectService);

  ngOnInit(): void {
    this.projectService.getProjects().subscribe({
      next: (data: any) => {
        this.projects.set(data);
      }
    });
  }
}