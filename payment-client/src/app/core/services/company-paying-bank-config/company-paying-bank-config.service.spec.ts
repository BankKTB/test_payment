import { TestBed } from '@angular/core/testing';

import { CompanyPayingBankConfigService } from './company-paying-bank-config.service';

describe('CompanyPayingBankConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyPayingBankConfigService = TestBed.get(CompanyPayingBankConfigService);
    expect(service).toBeTruthy();
  });
});
