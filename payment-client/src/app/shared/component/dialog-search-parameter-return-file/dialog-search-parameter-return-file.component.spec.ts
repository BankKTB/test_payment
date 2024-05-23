import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchParameterReturnFileComponent } from './dialog-search-parameter-return-file.component';

describe('DialogSearchParameterGenerateFileComponent', () => {
  let component: DialogSearchParameterReturnFileComponent;
  let fixture: ComponentFixture<DialogSearchParameterReturnFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchParameterReturnFileComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchParameterReturnFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
