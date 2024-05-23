import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogTaxFeeComponent } from './dialog-tax-fee.component';

describe('DialogTaxFeeComponent', () => {
  let component: DialogTaxFeeComponent;
  let fixture: ComponentFixture<DialogTaxFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogTaxFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogTaxFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
