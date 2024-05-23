import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogEditSwiftFeeComponent } from './dialog-edit-swift-fee.component';

describe('DialogEditSwiftFeeComponent', () => {
  let component: DialogEditSwiftFeeComponent;
  let fixture: ComponentFixture<DialogEditSwiftFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogEditSwiftFeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogEditSwiftFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
