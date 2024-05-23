import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmSpecialTypeComponent } from './dialog-om-special-type.component';

describe('DialogOmSpecialTypeComponent', () => {
  let component: DialogOmSpecialTypeComponent;
  let fixture: ComponentFixture<DialogOmSpecialTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmSpecialTypeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmSpecialTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
