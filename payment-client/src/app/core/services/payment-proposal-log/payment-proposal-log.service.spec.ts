import { TestBed } from '@angular/core/testing';

import { PaymentProposalLogService } from './payment-proposal-log.service';

describe('PaymentProposalLogService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaymentProposalLogService = TestBed.get(PaymentProposalLogService);
    expect(service).toBeTruthy();
  });
});
