import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSaveSwiftFeeComponent } from './dialog-save-swift-fee.component';

describe('DialogSaveSwiftFeeComponent', () => {
  let component: DialogSaveSwiftFeeComponent;
  let fixture: ComponentFixture<DialogSaveSwiftFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSaveSwiftFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSaveSwiftFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
