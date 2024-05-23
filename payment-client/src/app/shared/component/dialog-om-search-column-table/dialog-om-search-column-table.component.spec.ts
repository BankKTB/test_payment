import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmSearchColumnTableComponent } from './dialog-om-search-column-table.component';

describe('DialogOmSearchColumnTableComponent', () => {
  let component: DialogOmSearchColumnTableComponent;
  let fixture: ComponentFixture<DialogOmSearchColumnTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmSearchColumnTableComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmSearchColumnTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
