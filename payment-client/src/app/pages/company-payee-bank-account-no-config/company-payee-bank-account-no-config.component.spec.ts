import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayeeBankAccountNoConfigComponent } from './company-payee-bank-account-no-config.component';

describe('CompanyPayeeBankAccountNoConfigComponent', () => {
  let component: CompanyPayeeBankAccountNoConfigComponent;
  let fixture: ComponentFixture<CompanyPayeeBankAccountNoConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayeeBankAccountNoConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayeeBankAccountNoConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
