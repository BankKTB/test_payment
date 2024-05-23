import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentReverseComponent } from './payment-reverse.component';

describe('PaymentReverseComponent', () => {
  let component: PaymentReverseComponent;
  let fixture: ComponentFixture<PaymentReverseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentReverseComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentReverseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
