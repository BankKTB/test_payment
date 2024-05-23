import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchJobParameterComponent } from './dialog-search-job-parameter.component';

describe('DialogSearchJobParameterComponent', () => {
  let component: DialogSearchJobParameterComponent;
  let fixture: ComponentFixture<DialogSearchJobParameterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchJobParameterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchJobParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
