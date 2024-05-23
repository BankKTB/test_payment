import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogShowProposalDocumentComponent } from './dialog-show-proposal-document.component';

describe('DialogShowProposalDocumentComponent', () => {
  let component: DialogShowProposalDocumentComponent;
  let fixture: ComponentFixture<DialogShowProposalDocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogShowProposalDocumentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogShowProposalDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
