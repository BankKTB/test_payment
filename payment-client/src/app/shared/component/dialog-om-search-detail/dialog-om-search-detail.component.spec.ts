import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmSearchDetailComponent } from './dialog-om-search-detail.component';

describe('DialogOmSearchDetailComponent', () => {
  let component: DialogOmSearchDetailComponent;
  let fixture: ComponentFixture<DialogOmSearchDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmSearchDetailComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmSearchDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
