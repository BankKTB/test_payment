import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableReturnReverseInvoiceComponent } from './table-return-reverse-invoice.component';

describe('TableReturnReverseInvoiceComponent', () => {
  let component: TableReturnReverseInvoiceComponent;
  let fixture: ComponentFixture<TableReturnReverseInvoiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TableReturnReverseInvoiceComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableReturnReverseInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
