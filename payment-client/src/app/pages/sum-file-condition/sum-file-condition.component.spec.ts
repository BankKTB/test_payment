import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SumFileConditionComponent } from './sum-file-condition.component';

describe('SumFileConditionComponent', () => {
  let component: SumFileConditionComponent;
  let fixture: ComponentFixture<SumFileConditionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SumFileConditionComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SumFileConditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
