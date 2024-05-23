import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogReturnParameterGenFileComponent } from './dialog-return-parameter-gen-file.component';

describe('DialogReturnParameterGenFileComponent', () => {
  let component: DialogReturnParameterGenFileComponent;
  let fixture: ComponentFixture<DialogReturnParameterGenFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogReturnParameterGenFileComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogReturnParameterGenFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
