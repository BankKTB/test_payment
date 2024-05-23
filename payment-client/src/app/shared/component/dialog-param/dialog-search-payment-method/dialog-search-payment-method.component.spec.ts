import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchPaymentMethodComponent } from './dialog-search-payment-method.component';

describe('DialogSearchPaymentMethodComponent', () => {
  let component: DialogSearchPaymentMethodComponent;
  let fixture: ComponentFixture<DialogSearchPaymentMethodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchPaymentMethodComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchPaymentMethodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
