import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProposalTr1Component } from './proposal-tr1.component';

describe('ProposalTr1Component', () => {
  let component: ProposalTr1Component;
  let fixture: ComponentFixture<ProposalTr1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProposalTr1Component],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProposalTr1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
