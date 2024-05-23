import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogVendorForPaymentComponent } from './dialog-vendor-for-payment.component';

describe('DialogVendorForPaymentComponent', () => {
  let component: DialogVendorForPaymentComponent;
  let fixture: ComponentFixture<DialogVendorForPaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogVendorForPaymentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogVendorForPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
