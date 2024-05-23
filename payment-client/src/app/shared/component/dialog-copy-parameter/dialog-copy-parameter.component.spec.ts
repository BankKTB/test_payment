import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCopyParameterComponent } from './dialog-copy-parameter.component';

describe('DialogCopyParameterComponent', () => {
  let component: DialogCopyParameterComponent;
  let fixture: ComponentFixture<DialogCopyParameterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogCopyParameterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCopyParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
