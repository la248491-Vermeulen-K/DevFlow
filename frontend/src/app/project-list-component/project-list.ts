import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ProjectService } from '../project-service';

@Component({
  selector: 'app-project-list',
  imports: [],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectList implements OnInit, OnDestroy {

  projects: any[] = [];

  projectSubscription = new Subscription();
  existSubscription = new Subscription();

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.existSubscription = this.projectService.isProjectExists(1).subscribe({
      next: (data: any) => {
        console.log('Project exists:', data);
      },
      error: (error) => {
        console.error('Error checking project existence:', error);
      }
    }); 
    this.projectSubscription = this.projectService.getProjects().subscribe({
      next: (data: any) => {
        this.projects = data;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des projets :', error);
      }
    });
  }

  ngOnDestroy(): void {
    this.existSubscription.unsubscribe();
    this.projectSubscription.unsubscribe();
  }
}