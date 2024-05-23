import { TestBed } from '@angular/core/testing';

import { PaymentAliasService } from './payment-independent.service';

describe('PaymentAliasService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaymentAliasService = TestBed.get(PaymentAliasService);
    expect(service).toBeTruthy();
  });
});
