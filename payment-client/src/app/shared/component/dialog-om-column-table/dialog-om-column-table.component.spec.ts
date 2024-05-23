import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmcolumnTableComponent } from './dialog-om-column-table.component';

describe('DialogOmcolumnTableComponent', () => {
  let component: DialogOmcolumnTableComponent;
  let fixture: ComponentFixture<DialogOmcolumnTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmcolumnTableComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmcolumnTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
