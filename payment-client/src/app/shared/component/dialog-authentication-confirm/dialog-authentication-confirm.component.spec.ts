import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogAuthenConfirmComponent } from './dialog-authentication-confirm.component';

describe('DialogAuthenConfirmComponent', () => {
  let component: DialogAuthenConfirmComponent;
  let fixture: ComponentFixture<DialogAuthenConfirmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogAuthenConfirmComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogAuthenConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
