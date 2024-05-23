import { TestBed } from '@angular/core/testing';

import { SumFileConditionService } from './sum-file-condition.service';

describe('SumFileConditionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SumFileConditionService = TestBed.get(SumFileConditionService);
    expect(service).toBeTruthy();
  });
});
