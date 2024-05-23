import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSmartFeeComponent } from './view-smart-fee.component';

describe('ViewSmartFeeComponent', () => {
  let component: ViewSmartFeeComponent;
  let fixture: ComponentFixture<ViewSmartFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSmartFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewSmartFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
