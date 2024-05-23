import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPoHistoryComponent } from './dialog-po-history.component';

describe('DialogPoHistoryComponent', () => {
  let component: DialogPoHistoryComponent;
  let fixture: ComponentFixture<DialogPoHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPoHistoryComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPoHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
