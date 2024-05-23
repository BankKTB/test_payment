import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateDocumentJuComponent } from './generate-document-ju.component';

describe('GenerateDocumentJuComponent', () => {
  let component: GenerateDocumentJuComponent;
  let fixture: ComponentFixture<GenerateDocumentJuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GenerateDocumentJuComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateDocumentJuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
