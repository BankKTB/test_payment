import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmFiAreaComponent } from './dialog-om-fi-area.component';

describe('DialogOmFiAreaComponent', () => {
  let component: DialogOmFiAreaComponent;
  let fixture: ComponentFixture<DialogOmFiAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmFiAreaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmFiAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
