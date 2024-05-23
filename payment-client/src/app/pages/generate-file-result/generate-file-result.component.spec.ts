import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateFileResultComponent } from './generate-file-result.component';

describe('GenerateFileResultComponent', () => {
  let component: GenerateFileResultComponent;
  let fixture: ComponentFixture<GenerateFileResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GenerateFileResultComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateFileResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
