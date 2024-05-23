import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSaveArrangeColumnComponent } from './dialog-save-arrange-column.component';

describe('DialogArrangeColumnComponent', () => {
  let component: DialogSaveArrangeColumnComponent;
  let fixture: ComponentFixture<DialogSaveArrangeColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSaveArrangeColumnComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSaveArrangeColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
