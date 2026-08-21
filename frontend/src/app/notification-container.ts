import { Component, inject } from '@angular/core';
import { NotificationService } from './notification-service';

@Component({
  selector: 'app-notification-container',
  template: `
    <div class="notifications" aria-live="polite" aria-atomic="true">
      @for (notification of notificationService.notifications(); track notification.id) {
        <div class="notification" [class.notification--error]="notification.type === 'error'">
          <span>{{ notification.message }}</span>
          <button type="button" (click)="notificationService.dismiss(notification.id)" aria-label="Dismiss notification">×</button>
        </div>
      }
    </div>
  `,
  styleUrl: './notification-container.scss',
})
export class NotificationContainerComponent {
  protected readonly notificationService = inject(NotificationService);
}
