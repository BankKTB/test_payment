import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogUploadChangeDocumentComponent } from './dialog-upload-change-document.component';

describe('DialogUploadChangeDocumentComponent', () => {
  let component: DialogUploadChangeDocumentComponent;
  let fixture: ComponentFixture<DialogUploadChangeDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogUploadChangeDocumentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogUploadChangeDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
