import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeDocumentComponent } from './change-document.component';

describe('ChangeDocumentComponent', () => {
  let component: ChangeDocumentComponent;
  let fixture: ComponentFixture<ChangeDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ChangeDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
