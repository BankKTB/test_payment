import { TestBed } from '@angular/core/testing';

import { CompanyPayingPayMethodConfigService } from './company-paying-pay-method-config.service';

describe('CompanyPayingPayMethodConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyPayingPayMethodConfigService = TestBed.get(
      CompanyPayingPayMethodConfigService
    );
    expect(service).toBeTruthy();
  });
});
