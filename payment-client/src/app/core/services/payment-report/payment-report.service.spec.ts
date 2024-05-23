import { TestBed } from '@angular/core/testing';

import { PaymentReportService } from './payment-report.service';

describe('PaymentReportService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaymentReportService = TestBed.get(PaymentReportService);
    expect(service).toBeTruthy();
  });
});
