import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSumFileConditionComponent } from './view-sum-file-condition.component';

describe('ViewSumFileConditionComponent', () => {
  let component: ViewSumFileConditionComponent;
  let fixture: ComponentFixture<ViewSumFileConditionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSumFileConditionComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewSumFileConditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
