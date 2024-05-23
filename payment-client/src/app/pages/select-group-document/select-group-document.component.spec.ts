import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectGroupDocumentComponent } from './select-group-document.component';

describe('SelectGroupDocumentComponent', () => {
  let component: SelectGroupDocumentComponent;
  let fixture: ComponentFixture<SelectGroupDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SelectGroupDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectGroupDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
