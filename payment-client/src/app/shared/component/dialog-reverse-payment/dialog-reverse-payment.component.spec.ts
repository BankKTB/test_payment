import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogReversePaymentComponent } from './dialog-reverse-payment.component';

describe('DialogReversePaymentComponent', () => {
  let component: DialogReversePaymentComponent;
  let fixture: ComponentFixture<DialogReversePaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogReversePaymentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogReversePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
