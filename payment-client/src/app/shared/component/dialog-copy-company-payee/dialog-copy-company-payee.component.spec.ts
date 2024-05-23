import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCopyCompanyPayeeComponent } from './dialog-copy-company-payee.component';

describe('DialogCopyCompanyPayeeComponent', () => {
  let component: DialogCopyCompanyPayeeComponent;
  let fixture: ComponentFixture<DialogCopyCompanyPayeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCopyCompanyPayeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCopyCompanyPayeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
