import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogDetailDocumentColumnTableComponent } from './dialog-detail-document-column-table.component';

describe('DialogDetailDocumentColumnTableComponent', () => {
  let component: DialogDetailDocumentColumnTableComponent;
  let fixture: ComponentFixture<DialogDetailDocumentColumnTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogDetailDocumentColumnTableComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogDetailDocumentColumnTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
