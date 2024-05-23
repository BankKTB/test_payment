import { TestBed } from '@angular/core/testing';
import { FiService } from './fi.service';

describe('FiService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FiService = TestBed.get(FiService);
    expect(service).toBeTruthy();
  });
});
