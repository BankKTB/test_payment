import { TestBed } from '@angular/core/testing';

import { CompanyPayingService } from './company-paying.service';

describe('CompanyPayingService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyPayingService = TestBed.get(CompanyPayingService);
    expect(service).toBeTruthy();
  });
});
