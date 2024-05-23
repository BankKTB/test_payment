import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPayingComponent } from './company-paying.component';

describe('CompanyPayingComponent', () => {
  let component: CompanyPayingComponent;
  let fixture: ComponentFixture<CompanyPayingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyPayingComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPayingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
