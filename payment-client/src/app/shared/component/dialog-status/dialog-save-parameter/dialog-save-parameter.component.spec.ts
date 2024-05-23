import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSaveParameterComponent } from './dialog-save-parameter.component';

describe('DialogSaveParameterComponent', () => {
  let component: DialogSaveParameterComponent;
  let fixture: ComponentFixture<DialogSaveParameterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSaveParameterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSaveParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
