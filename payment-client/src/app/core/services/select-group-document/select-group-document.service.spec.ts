import { TestBed } from '@angular/core/testing';

import { SelectGroupDocumentService } from './select-group-document.service';

describe('SelectGroupDocumentService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SelectGroupDocumentService = TestBed.get(SelectGroupDocumentService);
    expect(service).toBeTruthy();
  });
});
