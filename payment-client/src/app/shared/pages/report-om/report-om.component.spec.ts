import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportOmComponent } from './report-om.component';

describe('ReportOmComponent', () => {
  let component: ReportOmComponent;
  let fixture: ComponentFixture<ReportOmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReportOmComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportOmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
