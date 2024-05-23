import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCompanyPayingConfigComponent } from './dialog-company-paying-config.component';

describe('DialogCompanyPayingConfigComponent', () => {
  let component: DialogCompanyPayingConfigComponent;
  let fixture: ComponentFixture<DialogCompanyPayingConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCompanyPayingConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCompanyPayingConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
