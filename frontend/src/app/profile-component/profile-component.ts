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

  profile = signal<UserProfile | null>(null);

  private userProfileService = inject(UserProfileService);

  ngOnInit(): void {
    this.userProfileService.getProfil().subscribe({
      next: (data) => {
        this.profile.set(data);
      },
      error: (error) => {
        console.error('Erreur lors de la récupération du profil', error);
      }
    });
  }
}