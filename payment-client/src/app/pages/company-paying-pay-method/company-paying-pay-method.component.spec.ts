import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayingPayMethodComponent } from './company-paying-pay-method.component';

describe('CompanyPayingPayMethodComponent', () => {
  let component: CompanyPayingPayMethodComponent;
  let fixture: ComponentFixture<CompanyPayingPayMethodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayingPayMethodComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayingPayMethodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
