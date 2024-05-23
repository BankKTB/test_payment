import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateFileReportComponent } from './generate-file-report.component';

describe('GenerateFileReportComponent', () => {
  let component: GenerateFileReportComponent;
  let fixture: ComponentFixture<GenerateFileReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GenerateFileReportComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateFileReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
