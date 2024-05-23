import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPreviewSelectGroupDocumentComponent } from './dialog-preview-select-group-document.component';

describe('DialogPreviewSelectGroupDocumentComponent', () => {
  let component: DialogPreviewSelectGroupDocumentComponent;
  let fixture: ComponentFixture<DialogPreviewSelectGroupDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPreviewSelectGroupDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPreviewSelectGroupDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
