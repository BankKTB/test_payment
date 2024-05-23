import { TestBed } from '@angular/core/testing';

import { ProposalTr1Service } from './proposal-tr1.service';

describe('ProposalTr1Service', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProposalTr1Service = TestBed.get(ProposalTr1Service);
    expect(service).toBeTruthy();
  });
});
