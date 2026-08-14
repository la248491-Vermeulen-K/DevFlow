import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { UserProfileService } from '../user-profile';
import { UserProfile } from '../models/userProfile-response';

@Component({
  selector: 'app-profile-component',
  imports: [DatePipe],
  templateUrl: './profile-component.html',
  styleUrl: './profile-component.scss',
})
export class ProfileComponent implements OnInit {

  backendUrl = "http://localhost:8080"

  errorMessage = ""

  profile = signal<UserProfile | null>(null);

  private userProfileService = inject(UserProfileService);

  ngOnInit(): void {
    this.userProfileService.getProfile().subscribe({
      next: (data) => {
        this.profile.set(data);
      },
      error: (error) => {
        console.error('Erreur lors de la récupération du profil', error);
      }
    });
  }

  onAvatarSelected(event: Event) {
  const input = event.target as HTMLInputElement;
  this.errorMessage = ""

  if (!input.files || input.files.length === 0) {
    return;
  }

  const file = input.files[0];
  this.userProfileService.uploadAvatar(file).subscribe({
  next: (updatedProfile) => {
    this.profile.set(updatedProfile);
  },
  error: (error) => {
    this.errorMessage = 'Erreur lors de l\'upload de l\'avatar'
    console.error('Erreur lors de l\'upload de l\'avatar', error);
  }
});
}
}