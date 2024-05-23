import { TestBed } from '@angular/core/testing';

import { SmartFeeService } from './smart-fee.service';

describe('SmartFeeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SmartFeeService = TestBed.get(SmartFeeService);
    expect(service).toBeTruthy();
  });
});
