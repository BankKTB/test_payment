import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSortColumnComponent } from './dialog-sort-column.component';

describe('DialogSortColumnComponent', () => {
  let component: DialogSortColumnComponent;
  let fixture: ComponentFixture<DialogSortColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSortColumnComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSortColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
