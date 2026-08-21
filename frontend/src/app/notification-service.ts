import { Injectable, signal } from '@angular/core';

export type NotificationType = 'success' | 'error';

export interface Notification {
  id: number;
  message: string;
  type: NotificationType;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  readonly notifications = signal<Notification[]>([]);
  private nextId = 0;

  success(message: string): void {
    this.show(message, 'success');
  }

  error(message: string): void {
    this.show(message, 'error');
  }

  dismiss(id: number): void {
    this.notifications.update(items => items.filter(item => item.id !== id));
  }

  private show(message: string, type: NotificationType): void {
    const notification = { id: ++this.nextId, message, type };
    this.notifications.update(items => [...items, notification]);
    window.setTimeout(() => this.dismiss(notification.id), 5000);
  }
}
