import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPoAdvancePaymentComponent } from './dialog-po-advance-payment.component';

describe('DialogPoAdvancePaymentComponent', () => {
  let component: DialogPoAdvancePaymentComponent;
  let fixture: ComponentFixture<DialogPoAdvancePaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPoAdvancePaymentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPoAdvancePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
