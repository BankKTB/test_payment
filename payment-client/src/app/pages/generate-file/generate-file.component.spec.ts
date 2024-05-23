import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateFileComponent } from './generate-file.component';

describe('GenerateFileComponent', () => {
  let component: GenerateFileComponent;
  let fixture: ComponentFixture<GenerateFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GenerateFileComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
