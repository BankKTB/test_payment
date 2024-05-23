import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {  } from './dialog-confirm-status-usability.component';

describe('', () => {
  let component: ;
  let fixture: ComponentFixture<DialogConfirmStatusUsabilityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogConfirmStatusUsabilityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogConfirmStatusUsabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
