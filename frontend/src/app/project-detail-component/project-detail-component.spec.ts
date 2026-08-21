import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { ProjectDetailComponent } from './project-detail-component';
import { ProjectMemberService } from '../project-member-service';
import { TaskService } from '../task-service';
import { LabelService } from '../label-service';

describe('ProjectDetailComponent', () => {
  let component: ProjectDetailComponent;
  let fixture: ComponentFixture<ProjectDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectDetailComponent],
      providers: [
        provideRouter([]),
        {
          provide: ProjectMemberService,
          useValue: { getMembers: () => of([]) },
        },
        {
          provide: TaskService,
          useValue: { getTasks: () => of([]) },
        },
        {
          provide: LabelService,
          useValue: { getLabels: () => of([]) },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProjectDetailComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('project', {
      id: 1,
      name: 'Demo project',
      description: 'Test project',
      createdAt: '2026-08-21T10:00:00',
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.projectId).toBe(1);
  });
});
