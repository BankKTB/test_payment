import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchParameterComponent } from './dialog-search-parameter.component';

describe('DialogSearchParameterComponent', () => {
  let component: DialogSearchParameterComponent;
  let fixture: ComponentFixture<DialogSearchParameterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchParameterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
