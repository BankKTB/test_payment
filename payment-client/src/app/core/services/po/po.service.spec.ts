import { TestBed } from '@angular/core/testing';

import { PoService } from './po.service';

describe('PoService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PoService = TestBed.get(PoService);
    expect(service).toBeTruthy();
  });
});
