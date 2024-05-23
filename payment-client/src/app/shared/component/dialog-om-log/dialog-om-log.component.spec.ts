import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmLogComponent } from './dialog-om-log.component';

describe('DialogOmLogComponent', () => {
  let component: DialogOmLogComponent;
  let fixture: ComponentFixture<DialogOmLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmLogComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
