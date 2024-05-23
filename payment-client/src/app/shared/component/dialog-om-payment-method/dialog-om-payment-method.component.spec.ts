import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmPaymentMethodComponent } from './dialog-om-payment-method.component';

describe('DialogOmPaymentMethodComponent', () => {
  let component: DialogOmPaymentMethodComponent;
  let fixture: ComponentFixture<DialogOmPaymentMethodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmPaymentMethodComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmPaymentMethodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
