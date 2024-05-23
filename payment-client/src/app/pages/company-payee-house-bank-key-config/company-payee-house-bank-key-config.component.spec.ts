import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayeeHouseBankKeyConfigComponent } from './company-payee-house-bank-key-config.component';

describe('CompanyPayeeHouseBankKeyConfigComponent', () => {
  let component: CompanyPayeeHouseBankKeyConfigComponent;
  let fixture: ComponentFixture<CompanyPayeeHouseBankKeyConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayeeHouseBankKeyConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayeeHouseBankKeyConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
