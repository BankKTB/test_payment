import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogConfirmChangeDocumentComponent } from './dialog-confirm-change-document.component';

describe('DialogConfirmChangeDocumentComponent', () => {
  let component: DialogConfirmChangeDocumentComponent;
  let fixture: ComponentFixture<DialogConfirmChangeDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogConfirmChangeDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogConfirmChangeDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
