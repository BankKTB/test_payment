import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCopyPaymentMethodConfigComponent } from './dialog-copy-payment-method-config.component';

describe('DialogCopyPaymentMethodConfigComponent', () => {
  let component: DialogCopyPaymentMethodConfigComponent;
  let fixture: ComponentFixture<DialogCopyPaymentMethodConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCopyPaymentMethodConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCopyPaymentMethodConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
