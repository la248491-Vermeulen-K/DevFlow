import { Component, signal } from '@angular/core';
import { ProjectMemberService } from '../project-member-service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-member-form-component',
  imports: [ReactiveFormsModule],
  templateUrl: './add-member-form-component.html',
  styleUrl: './add-member-form-component.scss',
})
export class AddMemberFormComponent {
  errorMessage = signal<string | null>(null);

  constructor(
    private memberService: ProjectMemberService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  addMemberForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    role: new FormControl('', Validators.required)
  });

  onSubmit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.errorMessage.set(null);

    if (this.addMemberForm.invalid) {
      console.error('Form is invalid');
      return;
    }

    const email = this.addMemberForm.get('email')?.value ?? '';
    const role = this.addMemberForm.get('role')?.value ?? '';
    alert(role)
    this.memberService.addMembers(id, email, role).subscribe({
        next: () => {
          this.router.navigate(["/projects", id])
        },
        error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/projects']);
        } else {
          this.errorMessage.set('Une erreur est survenue lors de l\'ajout du membre');
        }
      }
      });
  }

  onCancel(){
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.router.navigate(["/projects", id])
  }
}