import { TestBed } from '@angular/core/testing';

import { PaymentBlockService } from './payment-block.service';

describe('PaymentBlockService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaymentBlockService = TestBed.get(PaymentBlockService);
    expect(service).toBeTruthy();
  });
});
