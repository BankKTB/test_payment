import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SwiftFeeComponent } from './swift-fee.component';

describe('SwiftFeeComponent', () => {
  let component: SwiftFeeComponent;
  let fixture: ComponentFixture<SwiftFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SwiftFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SwiftFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
