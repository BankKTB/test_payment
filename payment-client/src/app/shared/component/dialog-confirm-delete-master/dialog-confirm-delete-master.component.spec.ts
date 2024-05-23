import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogConfirmDeleteMasterComponent } from './dialog-confirm-delete-master.component';

describe('DialogConfirmDeleteMasterComponent', () => {
  let component: DialogConfirmDeleteMasterComponent;
  let fixture: ComponentFixture<DialogConfirmDeleteMasterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogConfirmDeleteMasterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogConfirmDeleteMasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
