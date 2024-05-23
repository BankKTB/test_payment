import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobSchedDetailComponent } from './job-sched-detail.component';

describe('JobSchedComponent', () => {
  let component: JobSchedDetailComponent;
  let fixture: ComponentFixture<JobSchedDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [JobSchedDetailComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobSchedDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
