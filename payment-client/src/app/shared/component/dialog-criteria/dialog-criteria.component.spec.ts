import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCriteriaComponent } from './dialog-criteria.component';

describe('DialogCriteriaComponent', () => {
  let component: DialogCriteriaComponent;
  let fixture: ComponentFixture<DialogCriteriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCriteriaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCriteriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
