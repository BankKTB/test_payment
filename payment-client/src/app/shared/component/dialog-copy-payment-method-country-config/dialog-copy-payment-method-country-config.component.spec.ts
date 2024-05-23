import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCopyPaymentMethodCountryConfigComponent } from './dialog-copy-payment-method-country-config.component';

describe('DialogCopyPaymentMethodCountryConfigComponent', () => {
  let component: DialogCopyPaymentMethodCountryConfigComponent;
  let fixture: ComponentFixture<DialogCopyPaymentMethodCountryConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCopyPaymentMethodCountryConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCopyPaymentMethodCountryConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
