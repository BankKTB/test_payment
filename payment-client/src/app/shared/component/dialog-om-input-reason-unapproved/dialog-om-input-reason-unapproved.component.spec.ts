import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmInputReasonUnapprovedComponent } from './dialog-om-input-reason-unapproved.component';

describe('DialogOmInputReasonUnapprovedComponent', () => {
  let component: DialogOmInputReasonUnapprovedComponent;
  let fixture: ComponentFixture<DialogOmInputReasonUnapprovedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogOmInputReasonUnapprovedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmInputReasonUnapprovedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
