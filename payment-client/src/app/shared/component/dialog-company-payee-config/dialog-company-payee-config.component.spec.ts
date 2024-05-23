import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCompanyPayeeConfigComponent } from './dialog-company-payee-config.component';

describe('DialogCompanyPayeeConfigComponent', () => {
  let component: DialogCompanyPayeeConfigComponent;
  let fixture: ComponentFixture<DialogCompanyPayeeConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCompanyPayeeConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCompanyPayeeConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
