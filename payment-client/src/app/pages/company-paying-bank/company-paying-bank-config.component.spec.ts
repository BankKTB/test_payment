import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayingBankConfigComponent } from './company-paying-bank-config.component';

describe('CompanyPayingBankConfigComponent', () => {
  let component: CompanyPayingBankConfigComponent;
  let fixture: ComponentFixture<CompanyPayingBankConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayingBankConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayingBankConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
