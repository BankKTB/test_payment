import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogBatchTransactionPaymentNameComponent } from './dialog-batch-transaction-payment-name.component';

describe('DialogBatchTransactionPaymentNameComponent', () => {
  let component: DialogBatchTransactionPaymentNameComponent;
  let fixture: ComponentFixture<DialogBatchTransactionPaymentNameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogBatchTransactionPaymentNameComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogBatchTransactionPaymentNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
