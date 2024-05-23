import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobSchedComponent } from './job-sched.component';

describe('JobSchedComponent', () => {
  let component: JobSchedComponent;
  let fixture: ComponentFixture<JobSchedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [JobSchedComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobSchedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
