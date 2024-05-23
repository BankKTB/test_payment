import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogDisplayArrangeColumnComponent } from './dialog-display-arrange-column.component';

describe('DialogArrangeColumnComponent', () => {
  let component: DialogDisplayArrangeColumnComponent;
  let fixture: ComponentFixture<DialogDisplayArrangeColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogDisplayArrangeColumnComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogDisplayArrangeColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
