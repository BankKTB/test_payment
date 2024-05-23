import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmSaveSearchCriteriaComponent } from './dialog-om-save-search-criteria.component';

describe('DialogOmSaveSearchCriteriaComponent', () => {
  let component: DialogOmSaveSearchCriteriaComponent;
  let fixture: ComponentFixture<DialogOmSaveSearchCriteriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmSaveSearchCriteriaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmSaveSearchCriteriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
