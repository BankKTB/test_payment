import { TestBed } from '@angular/core/testing';

import { PayMethodCurrencyConfigService } from './pay-method-currency-config.service';

describe('PayMethodCurrencyConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PayMethodCurrencyConfigService = TestBed.get(PayMethodCurrencyConfigService);
    expect(service).toBeTruthy();
  });
});
