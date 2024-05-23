import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSwiftFeeComponent } from './view-swift-fee.component';

describe('ViewSwiftFeeComponent', () => {
  let component: ViewSwiftFeeComponent;
  let fixture: ComponentFixture<ViewSwiftFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSwiftFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewSwiftFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
