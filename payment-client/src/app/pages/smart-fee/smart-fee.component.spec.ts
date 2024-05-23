import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SmartFeeComponent } from './smart-fee.component';

describe('SmartFeeComponent', () => {
  let component: SmartFeeComponent;
  let fixture: ComponentFixture<SmartFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SmartFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SmartFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
