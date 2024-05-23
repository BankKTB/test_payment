import { TestBed } from '@angular/core/testing';

import { CompanyPayeeHouseBankKeyConfigService } from './company-payee-house-bank-key-config.service';

describe('CompanyPayeeHouseBankKeyConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyPayeeHouseBankKeyConfigService = TestBed.get(
      CompanyPayeeHouseBankKeyConfigService
    );
    expect(service).toBeTruthy();
  });
});
