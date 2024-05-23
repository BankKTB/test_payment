import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayeeHouseBankKeyDetailConfigComponent } from './company-payee-house-bank-key-detail-config.component';

describe('CompanyPayeeHouseBankKeyDetailConfigComponent', () => {
  let component: CompanyPayeeHouseBankKeyDetailConfigComponent;
  let fixture: ComponentFixture<CompanyPayeeHouseBankKeyDetailConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayeeHouseBankKeyDetailConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayeeHouseBankKeyDetailConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
