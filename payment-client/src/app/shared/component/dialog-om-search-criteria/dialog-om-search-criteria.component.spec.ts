import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmSearchCriteriaComponent } from './dialog-om-search-criteria.component';

describe('DialogOmSearchCriteriaComponent', () => {
  let component: DialogOmSearchCriteriaComponent;
  let fixture: ComponentFixture<DialogOmSearchCriteriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmSearchCriteriaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmSearchCriteriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
