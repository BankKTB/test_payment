import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmVendorComponent } from './dialog-om-vendor.component';

describe('DialogOmVendorComponent', () => {
  let component: DialogOmVendorComponent;
  let fixture: ComponentFixture<DialogOmVendorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmVendorComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmVendorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
