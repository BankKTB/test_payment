import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogFilterColumnComponent } from './dialog-filter-column.component';

describe('DialogFilterColumnComponent', () => {
  let component: DialogFilterColumnComponent;
  let fixture: ComponentFixture<DialogFilterColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogFilterColumnComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogFilterColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
