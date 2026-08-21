import { Component, signal } from '@angular/core';
import { ProjectService } from '../project-service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-project-form-component',
  imports: [ReactiveFormsModule],
  templateUrl: './project-form-component.html',
  styleUrl: './project-form-component.scss',
})
export class ProjectFormComponent {
  errorMessage = signal<string | null>(null);

  constructor(
    private projectService: ProjectService,
    private router: Router
  ) {}

  createProjectForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('')
  });

  onSubmit() {
    const name = this.createProjectForm.get('name')?.value ?? '';
    const description = this.createProjectForm.get('description')?.value ?? '';

    this.projectService.createProject(name,description).subscribe({
      next: () => {
        this.router.navigate(['/projects']);
      },
      error: () => {
        this.errorMessage.set('Invalid name or description.');
      }
    })
  }

  onCancel(){
    this.router.navigate(["/projects"])
  }
}
