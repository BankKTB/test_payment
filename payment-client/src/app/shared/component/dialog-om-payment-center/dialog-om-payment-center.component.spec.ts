import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmPaymentCenterComponent } from './dialog-om-payment-center.component';

describe('DialogOmPaymentCenterComponent', () => {
  let component: DialogOmPaymentCenterComponent;
  let fixture: ComponentFixture<DialogOmPaymentCenterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmPaymentCenterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmPaymentCenterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
