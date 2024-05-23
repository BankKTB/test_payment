import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogReturnLogComponent } from './dialog-return-log.component';

describe('DialogReturnLogComponent', () => {
  let component: DialogReturnLogComponent;
  let fixture: ComponentFixture<DialogReturnLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogReturnLogComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogReturnLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
