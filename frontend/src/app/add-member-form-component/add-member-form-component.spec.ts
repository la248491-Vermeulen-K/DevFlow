import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMemberFormComponent } from './add-member-form-component';

describe('AddMemberFormComponent', () => {
  let component: AddMemberFormComponent;
  let fixture: ComponentFixture<AddMemberFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddMemberFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AddMemberFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
