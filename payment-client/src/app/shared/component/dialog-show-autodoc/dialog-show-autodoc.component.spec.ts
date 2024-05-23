import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogShowAutodocComponent } from './dialog-show-autodoc.component';

describe('DialogShowAutodocComponent', () => {
  let component: DialogShowAutodocComponent;
  let fixture: ComponentFixture<DialogShowAutodocComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogShowAutodocComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogShowAutodocComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
