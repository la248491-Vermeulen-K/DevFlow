import { Component, OnInit, signal } from '@angular/core';
import { ProjectMemberResponse } from '../models/project-member-response';
import { ProjectMemberService } from '../project-member-service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AddMemberFormComponent } from "../add-member-form-component/add-member-form-component";

@Component({
  selector: 'app-project-detail-component',
  imports: [DatePipe, AddMemberFormComponent],
  templateUrl: './project-detail-component.html',
  styleUrl: './project-detail-component.scss',
})
export class ProjectDetailComponent implements OnInit{
  projectMembersList = signal< ProjectMemberResponse[]>([]);
  backendUrl = "http://localhost:8080"
  showAddMemberForm = signal(false);
  projectIdFromRoute!: number

  errorMessage = signal<string | null>(null);
  
  constructor(
    private projectMemberService: ProjectMemberService,
    private route: ActivatedRoute,
    private router: Router,
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
  }
  backButton(){
    this.router.navigate(['/projects'])
  }

  addMemberButton(){
    this.showAddMemberForm.set(true)
  }

  onMemberAdded(member: ProjectMemberResponse): void {
    this.projectMembersList.update(members => [
      ...members,
      member
    ]);

    this.showAddMemberForm.set(false);
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
}
