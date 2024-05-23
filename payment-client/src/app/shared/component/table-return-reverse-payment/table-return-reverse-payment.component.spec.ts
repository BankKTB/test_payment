import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableReturnReversePaymentComponent } from './table-return-reverse-payment.component';

describe('TableReturnReversePaymentComponent', () => {
  let component: TableReturnReversePaymentComponent;
  let fixture: ComponentFixture<TableReturnReversePaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TableReturnReversePaymentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableReturnReversePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
