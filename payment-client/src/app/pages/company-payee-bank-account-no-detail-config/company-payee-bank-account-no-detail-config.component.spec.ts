import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayeeBankAccountNoDetailConfigComponent } from './company-payee-bank-account-no-detail-config.component';

describe('CompanyPayeeBankAccountNoDetailConfigComponent', () => {
  let component: CompanyPayeeBankAccountNoDetailConfigComponent;
  let fixture: ComponentFixture<CompanyPayeeBankAccountNoDetailConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayeeBankAccountNoDetailConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayeeBankAccountNoDetailConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
