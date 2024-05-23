import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchVendorComponent } from './dialog-search-vendor.component';

describe('DialogSearchVendorComponent', () => {
  let component: DialogSearchVendorComponent;
  let fixture: ComponentFixture<DialogSearchVendorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchVendorComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchVendorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
