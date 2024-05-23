import { TestBed } from '@angular/core/testing';

import { CompanyPayeeService } from './company-payee.service';

describe('CompanyPayeeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyPayeeService = TestBed.get(CompanyPayeeService);
    expect(service).toBeTruthy();
  });
});
