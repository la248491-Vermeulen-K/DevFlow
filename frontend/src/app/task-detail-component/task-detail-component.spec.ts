import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskDetailComponent } from './task-detail-component';
import { Task } from '../models/task-reponse';

describe('TaskDetailComponent', () => {
  let component: TaskDetailComponent;
  let fixture: ComponentFixture<TaskDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TaskDetailComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('task', {
      id: 1,
      title: 'Prepare sprint planning',
      description: null,
      status: 'TODO',
      createdAt: '2026-08-20T10:00:00',
      assigneeId: null,
      assigneeName: null,
      deadline: null,
      priority: 'MEDIUM',
      labels: [],
    } satisfies Task);
    fixture.componentRef.setInput('projectMembers', []);
    fixture.componentRef.setInput('availableLabels', []);
    fixture.componentRef.setInput('projectId', 1);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.title).toBe('Prepare sprint planning');
  });
});
