import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmSaveColumnTableComponent } from './dialog-om-save-column-table.component';

describe('DialogOmSaveColumnTableComponent', () => {
  let component: DialogOmSaveColumnTableComponent;
  let fixture: ComponentFixture<DialogOmSaveColumnTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmSaveColumnTableComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmSaveColumnTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
