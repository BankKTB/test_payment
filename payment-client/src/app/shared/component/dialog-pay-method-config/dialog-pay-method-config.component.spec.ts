import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPayMethodConfigComponent } from './dialog-pay-method-config.component';

describe('DialogPayMethodConfigComponent', () => {
  let component: DialogPayMethodConfigComponent;
  let fixture: ComponentFixture<DialogPayMethodConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPayMethodConfigComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPayMethodConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
