import { TestBed } from '@angular/core/testing';

import { BatchJobService } from './batch-job.service';

describe('BatchJobService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BatchJobService = TestBed.get(BatchJobService);
    expect(service).toBeTruthy();
  });
});
