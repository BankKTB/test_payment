import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogEditSmartFeeComponent } from './dialog-edit-smart-fee.component';

describe('DialogEditSmartFeeComponent', () => {
  let component: DialogEditSmartFeeComponent;
  let fixture: ComponentFixture<DialogEditSmartFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogEditSmartFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogEditSmartFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
