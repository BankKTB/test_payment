import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmResultESummaryComponent } from './om-result-e-summary.component';

describe('OmResultESummaryComponent', () => {
  let component: OmResultESummaryComponent;
  let fixture: ComponentFixture<OmResultESummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmResultESummaryComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmResultESummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
