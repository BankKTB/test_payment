import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchFieldComponent } from './dialog-search-field.component';

describe('DialogSearchFieldComponent', () => {
  let component: DialogSearchFieldComponent;
  let fixture: ComponentFixture<DialogSearchFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchFieldComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
