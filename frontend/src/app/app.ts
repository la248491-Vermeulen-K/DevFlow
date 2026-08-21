import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NotificationContainerComponent } from './notification-container';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NotificationContainerComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('frontend');
}
