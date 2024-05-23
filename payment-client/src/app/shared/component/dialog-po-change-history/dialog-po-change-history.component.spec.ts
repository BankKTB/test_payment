import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPoChangeHistoryComponent } from './dialog-po-change-history.component';

describe('DialogPoChangeHistoryComponent', () => {
  let component: DialogPoChangeHistoryComponent;
  let fixture: ComponentFixture<DialogPoChangeHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPoChangeHistoryComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPoChangeHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
