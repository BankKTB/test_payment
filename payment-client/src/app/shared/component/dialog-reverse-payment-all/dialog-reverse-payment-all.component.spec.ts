import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogReversePaymentAllComponent } from './dialog-reverse-payment-all.component';

describe('DialogReversePaymentAllComponent', () => {
  let component: DialogReversePaymentAllComponent;
  let fixture: ComponentFixture<DialogReversePaymentAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogReversePaymentAllComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogReversePaymentAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
