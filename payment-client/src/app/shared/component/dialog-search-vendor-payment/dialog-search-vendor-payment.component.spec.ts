import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchVendorPaymentComponent } from './dialog-search-vendor-payment.component';

describe('DialogSearchVendorPaymentComponent', () => {
  let component: DialogSearchVendorPaymentComponent;
  let fixture: ComponentFixture<DialogSearchVendorPaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchVendorPaymentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchVendorPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
