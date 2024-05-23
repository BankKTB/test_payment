import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmCompanyCodeComponent } from './dialog-om-company-code.component';

describe('DialogOmCompanyCodeComponent', () => {
  let component: DialogOmCompanyCodeComponent;
  let fixture: ComponentFixture<DialogOmCompanyCodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmCompanyCodeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmCompanyCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
