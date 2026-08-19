import { Component, OnInit, signal } from '@angular/core';
import { ProjectMemberResponse } from '../models/project-member-response';
import { ProjectMemberService } from '../project-member-service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AddMemberFormComponent } from "../add-member-form-component/add-member-form-component";
import { TaskService } from '../task-service';
import { Task } from '../models/task-reponse';
import { TaskFormComponent } from '../task-form-component/task-form-component';

@Component({
  selector: 'app-project-detail-component',
  imports: [DatePipe, AddMemberFormComponent, TaskFormComponent],
  templateUrl: './project-detail-component.html',
  styleUrl: './project-detail-component.scss',
})
export class ProjectDetailComponent implements OnInit{
  projectMembersList = signal< ProjectMemberResponse[]>([]);
  tasksList = signal< Task[]>([]);
  backendUrl = "http://localhost:8080"
  viewMode = signal<'list' | 'addMember' | 'addTask'>('list');
  projectIdFromRoute!: number

  errorMessage = signal<string | null>(null);
  
  constructor(
    private projectMemberService: ProjectMemberService,
    private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService 
  ){}
  

  ngOnInit(){
    this.projectIdFromRoute = Number(this.route.snapshot.paramMap.get('id'));
    this.projectMemberService.getMembers(this.projectIdFromRoute).subscribe({
      next: (data: ProjectMemberResponse[]) => {
          this.projectMembersList.set(data);
      },
      error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/projects']);
        } else {
          this.errorMessage.set('Une erreur est survenue lors du chargement des membres');
        }
      }
    })
    this.taskService.getTasks(this.projectIdFromRoute).subscribe({
      next: (data: Task[]) => {
          this.tasksList.set(data);
      },
      error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/projects']);
        } else {
          this.errorMessage.set('Une erreur est survenue lors du chargement des tâches');
        }
      }
    })
  }

  backButton(){
    this.router.navigate(['/projects'])
  }

  addMemberButton(){
    this.viewMode.set('addMember');
  }

  addTaskButton(){
    this.viewMode.set('addTask');
  }

  onMemberAdded(member: ProjectMemberResponse): void {
    this.projectMembersList.update(members => [
      ...members,
      member
    ]);

    this.viewMode.set('list');
  }

  onTaskAdded(task: Task): void{
    this.tasksList.update(tasks =>[
      ...tasks,
      task
    ]);
    this.viewMode.set('list');
  }

  onStatusChange(taskId: number, newStatus: string): void {
    this.taskService.updateStatus(taskId, newStatus).subscribe({
      next: (updatedTask: Task) => {
        this.tasksList.update(tasks =>
          tasks.map(task =>
            task.id === taskId ? updatedTask : task
          )
        );
      },
      error: (error) => {
        console.error('Erreur lors de la modification du statut :', error);
        this.errorMessage.set(
          'Impossible de modifier le statut de la tâche.'
        );
      }
    });
  }

  deleteProjectMember(userId: number): void {
    const projectId = Number(this.route.snapshot.paramMap.get('id'));

    this.projectMemberService.removeMember(projectId, userId).subscribe({
      next: () => {
        this.projectMembersList.update(members =>
          members.filter(member => member.id !== userId)
        );
      },
      error: (error) => {
        console.error('Erreur lors de la suppression du membre :', error);

        this.errorMessage.set(
          'Impossible de supprimer le membre.'
        );
      }
    });
  }

  onAssign(taskId: number, email: string): void {
    this.taskService.assignTask(taskId, email).subscribe({
      next: (updatedTask: Task) => {
        this.tasksList.update(tasks =>
          tasks.map(task =>
            task.id === taskId ? updatedTask : task
          )
        );
      },
      error: (error) => {
        console.error('Erreur lors de l’assignation de la tâche :', error);
        this.errorMessage.set(
          'Impossible d’assigner la tâche.'
        );
      }
    });
  }

  deleteTask(taskId: number): void {
    this.taskService.deleteTask(taskId).subscribe({
      next: () => {
        this.tasksList.update(tasks => tasks.filter(task => task.id !== taskId));
      },
      error: (error) => {
        console.error('Erreur lors de la suppression de la tâche :', error);
        this.errorMessage.set('Impossible de supprimer la tâche.');
      }
    });
  }

  showProfile(id: number){
    //add backend endpoint first 
  }
}
