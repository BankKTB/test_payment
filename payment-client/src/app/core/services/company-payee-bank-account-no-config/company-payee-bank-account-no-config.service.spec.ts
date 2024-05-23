import { TestBed } from '@angular/core/testing';

import { CompanyPayeeBankAccountNoConfigService } from './company-payee-bank-account-no-config.service';

describe('CompanyPayeeBankAccountNoConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyPayeeBankAccountNoConfigService = TestBed.get(
      CompanyPayeeBankAccountNoConfigService
    );
    expect(service).toBeTruthy();
  });
});
