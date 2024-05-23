import { TestBed } from '@angular/core/testing';

import { GenerateFileService } from './generate-file.service';

describe('GenerateFileService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GenerateFileService = TestBed.get(GenerateFileService);
    expect(service).toBeTruthy();
  });
});
