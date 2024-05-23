import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogRunProcessComponent } from './dialog-run-process.component';

describe('DialogRunProcessComponent', () => {
  let component: DialogRunProcessComponent;
  let fixture: ComponentFixture<DialogRunProcessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogRunProcessComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogRunProcessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
