import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayeeComponent } from './company-payee.component';

describe('CompanyPayeeComponent', () => {
  let component: CompanyPayeeComponent;
  let fixture: ComponentFixture<CompanyPayeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayeeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
