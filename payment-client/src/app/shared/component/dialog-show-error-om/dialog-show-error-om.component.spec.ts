import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogShowErrorOmComponent } from './dialog-show-error-om.component';

describe('DialogShowErrorOmComponent', () => {
  let component: DialogShowErrorOmComponent;
  let fixture: ComponentFixture<DialogShowErrorOmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogShowErrorOmComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogShowErrorOmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
