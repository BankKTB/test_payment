import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogArrangeColumnComponent } from './dialog-arrange-column.component';

describe('DialogArrangeColumnComponent', () => {
  let component: DialogArrangeColumnComponent;
  let fixture: ComponentFixture<DialogArrangeColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogArrangeColumnComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogArrangeColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
