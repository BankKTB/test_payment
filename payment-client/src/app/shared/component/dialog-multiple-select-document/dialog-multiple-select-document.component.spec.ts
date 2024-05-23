import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogMultipleSelectDocumentComponent } from './dialog-multiple-select-document.component';

describe('DialogMultipleSelectDocumentComponent', () => {
  let component: DialogMultipleSelectDocumentComponent;
  let fixture: ComponentFixture<DialogMultipleSelectDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogMultipleSelectDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogMultipleSelectDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
