import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSaveSmartFeeComponent } from './dialog-save-smart-fee.component';

describe('DialogSaveSmartFeeComponent', () => {
  let component: DialogSaveSmartFeeComponent;
  let fixture: ComponentFixture<DialogSaveSmartFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSaveSmartFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSaveSmartFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
