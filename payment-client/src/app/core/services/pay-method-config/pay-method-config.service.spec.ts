import { TestBed } from '@angular/core/testing';

import { PayMethodConfigService } from './pay-method-config.service';

describe('PayMethodConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PayMethodConfigService = TestBed.get(PayMethodConfigService);
    expect(service).toBeTruthy();
  });
});
