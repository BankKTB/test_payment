import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayingPayMethodConfigComponent } from './company-paying-pay-method-config.component';

describe('CompanyPayingPayMethodConfigComponent', () => {
  let component: CompanyPayingPayMethodConfigComponent;
  let fixture: ComponentFixture<CompanyPayingPayMethodConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayingPayMethodConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayingPayMethodConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
