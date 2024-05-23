import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogUpdateArrangeColumnComponent } from './dialog-update-arrange-column.component';

describe('DialogArrangeColumnComponent', () => {
  let component: DialogUpdateArrangeColumnComponent;
  let fixture: ComponentFixture<DialogUpdateArrangeColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogUpdateArrangeColumnComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogUpdateArrangeColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
