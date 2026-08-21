import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { UserProfileService } from '../user-profile';
import { UserProfile } from '../models/userProfile-response';
import { NotificationService } from '../notification-service';

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
  private notifications = inject(NotificationService);

  ngOnInit(): void {
    this.userProfileService.getProfile().subscribe({
      next: (data) => {
        this.profile.set(data);
      },
      error: () => {
        this.notifications.error('Unable to load your profile.');
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
  error: () => {
    this.errorMessage = 'Unable to upload your profile picture.';
    this.notifications.error('Unable to update your profile picture.');
  }
});
}
}
