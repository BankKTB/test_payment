import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmResultSummaryComponent } from './om-result-summary.component';

describe('OmResultSummaryComponent', () => {
  let component: OmResultSummaryComponent;
  let fixture: ComponentFixture<OmResultSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmResultSummaryComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmResultSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
