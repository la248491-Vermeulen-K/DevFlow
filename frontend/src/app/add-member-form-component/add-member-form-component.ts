import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { ProjectMemberService } from '../project-member-service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectMemberResponse } from '../models/project-member-response';

@Component({
  selector: 'app-add-member-form-component',
  imports: [ReactiveFormsModule],
  templateUrl: './add-member-form-component.html',
  styleUrl: './add-member-form-component.scss',
})
export class AddMemberFormComponent {
  errorMessage = signal<string | null>(null);

  @Input({required: true}) projectId!: number;
  @Output() memberAdded = new EventEmitter<ProjectMemberResponse>();
  @Output() cancelled = new EventEmitter<void>();

  constructor(
    private memberService: ProjectMemberService,
  ) {}

  addMemberForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    role: new FormControl('', Validators.required)
  });

  onSubmit() {
    this.errorMessage.set(null);

    if (this.addMemberForm.invalid) {
      console.error('Form is invalid');
      return; 
    }

    const email = this.addMemberForm.get('email')?.value ?? '';
    const role = this.addMemberForm.get('role')?.value ?? '';
    this.memberService.addMember(this.projectId, email, role).subscribe({
        next: (response: ProjectMemberResponse) => {
          this.memberAdded.emit(response);
        },
        error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.errorMessage.set('You do not have permission to add a member to this project.');
        } else {
          this.errorMessage.set('An error occurred while adding the member.');
        }
      }
      });
  }

  onCancel(){
    this.cancelled.emit()
  }
}
