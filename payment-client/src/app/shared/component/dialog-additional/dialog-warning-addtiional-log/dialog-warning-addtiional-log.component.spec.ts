import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogWarningAddtiionalLogComponent } from './dialog-warning-addtiional-log.component';

describe('DialogWarningAddtiionalLogComponent', () => {
  let component: DialogWarningAddtiionalLogComponent;
  let fixture: ComponentFixture<DialogWarningAddtiionalLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogWarningAddtiionalLogComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogWarningAddtiionalLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
