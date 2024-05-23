import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPaymentRunErrorDetailComponent } from './dialog-payment-run-error-detail.component';

describe('DialogPaymentRunErrorDetailComponent', () => {
  let component: DialogPaymentRunErrorDetailComponent;
  let fixture: ComponentFixture<DialogPaymentRunErrorDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPaymentRunErrorDetailComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPaymentRunErrorDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
