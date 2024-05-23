import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentRunErrorComponent } from './payment-run-error.component';

describe('PaymentRunErrorComponent', () => {
  let component: PaymentRunErrorComponent;
  let fixture: ComponentFixture<PaymentRunErrorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentRunErrorComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentRunErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
