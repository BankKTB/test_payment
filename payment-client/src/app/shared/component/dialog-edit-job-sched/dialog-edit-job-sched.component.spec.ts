import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogEditJobSchedComponent } from './dialog-edit-job-sched.component';

describe('DialogEditJobSchedComponent', () => {
  let component: DialogEditJobSchedComponent;
  let fixture: ComponentFixture<DialogEditJobSchedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogEditJobSchedComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogEditJobSchedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
