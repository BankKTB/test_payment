import { TestBed } from '@angular/core/testing';

import { SwiftFeeService } from './swift-fee.service';

describe('SwiftFeeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SwiftFeeService = TestBed.get(SwiftFeeService);
    expect(service).toBeTruthy();
  });
});
