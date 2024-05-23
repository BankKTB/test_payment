import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSaveSumFileConditionComponent } from './dialog-save-sum-file-condition.component';

describe('DialogSaveSumFileConditionComponent', () => {
  let component: DialogSaveSumFileConditionComponent;
  let fixture: ComponentFixture<DialogSaveSumFileConditionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSaveSumFileConditionComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSaveSumFileConditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
