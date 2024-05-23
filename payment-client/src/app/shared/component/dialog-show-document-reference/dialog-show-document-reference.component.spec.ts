import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogShowDocumentReferenceComponent } from './dialog-show-document-reference.component';

describe('DialogShowDocumentReferenceComponent', () => {
  let component: DialogShowDocumentReferenceComponent;
  let fixture: ComponentFixture<DialogShowDocumentReferenceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogShowDocumentReferenceComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogShowDocumentReferenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
