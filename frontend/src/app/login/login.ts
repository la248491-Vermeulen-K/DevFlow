import { Component, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  errorMessage = signal<string | null>(null);

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  });

  onSubmit() {
    this.errorMessage.set(null);

    if (this.loginForm.invalid) {
      console.error('Form is invalid');
      return;
    }

    const email = this.loginForm.get('email')?.value ?? '';
    const password = this.loginForm.get('password')?.value ?? '';

    this.authService.login(email, password).subscribe({
      next: () => {
        this.router.navigate(['/projects']);
      },
      error: () => {
        this.errorMessage.set('Email ou mot de passe incorrect');
      }
    });
  }
}
