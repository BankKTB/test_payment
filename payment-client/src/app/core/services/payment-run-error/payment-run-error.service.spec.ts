import { TestBed } from '@angular/core/testing';

import { PaymentRunErrorService } from './payment-run-error.service';

describe('PaymentRunErrorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaymentRunErrorService = TestBed.get(PaymentRunErrorService);
    expect(service).toBeTruthy();
  });
});
