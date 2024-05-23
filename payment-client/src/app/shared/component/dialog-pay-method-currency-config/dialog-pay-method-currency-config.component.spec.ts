import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPayMethodCurrencyConfigComponent } from './dialog-pay-method-currency-config.component';

describe('DialogPayMethodCurrencyConfigComponent', () => {
  let component: DialogPayMethodCurrencyConfigComponent;
  let fixture: ComponentFixture<DialogPayMethodCurrencyConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPayMethodCurrencyConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPayMethodCurrencyConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
