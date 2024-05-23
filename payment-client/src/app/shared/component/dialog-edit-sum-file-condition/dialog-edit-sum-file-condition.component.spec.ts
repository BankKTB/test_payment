import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogEditSumFileConditionComponent } from './dialog-edit-sum-file-condition.component';

describe('DialogEditSumFileConditionComponent', () => {
  let component: DialogEditSumFileConditionComponent;
  let fixture: ComponentFixture<DialogEditSumFileConditionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogEditSumFileConditionComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogEditSumFileConditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
