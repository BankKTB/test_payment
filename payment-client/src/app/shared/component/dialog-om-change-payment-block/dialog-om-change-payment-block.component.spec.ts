import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmChangePaymentBlockComponent } from './dialog-om-change-payment-block.component';

describe('DialogOmChangePaymentBlockComponent', () => {
  let component: DialogOmChangePaymentBlockComponent;
  let fixture: ComponentFixture<DialogOmChangePaymentBlockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmChangePaymentBlockComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmChangePaymentBlockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
