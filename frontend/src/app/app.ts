import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ProjectListComponent } from "./project-list-component/project-list-component";
@Component({
  selector: 'app-root',
  imports: [ProjectListComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('frontend');
}
