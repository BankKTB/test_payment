import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogDetailDocumentComponent } from './dialog-detail-document.component';

describe('DialogDetailDocumentComponent', () => {
  let component: DialogDetailDocumentComponent;
  let fixture: ComponentFixture<DialogDetailDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogDetailDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogDetailDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
