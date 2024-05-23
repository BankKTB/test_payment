import { TestBed } from '@angular/core/testing';

import { GenerateJuService } from './generate-ju.service';

describe('GenerateJuService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GenerateJuService = TestBed.get(GenerateJuService);
    expect(service).toBeTruthy();
  });
});
