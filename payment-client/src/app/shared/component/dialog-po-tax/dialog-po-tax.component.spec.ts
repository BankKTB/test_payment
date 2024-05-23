import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPoTaxComponent } from './dialog-po-tax.component';

describe('DialogPoTaxComponent', () => {
  let component: DialogPoTaxComponent;
  let fixture: ComponentFixture<DialogPoTaxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPoTaxComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPoTaxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
