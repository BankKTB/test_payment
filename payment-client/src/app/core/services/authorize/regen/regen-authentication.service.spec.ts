import { TestBed } from '@angular/core/testing';

import { RegenAuthenticationService } from './regen-authentication.service';

describe('RegenAuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RegenAuthenticationService = TestBed.get(RegenAuthenticationService);
    expect(service).toBeTruthy();
  });
});
