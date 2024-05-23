import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchParameterGenerateFileComponent } from './dialog-search-parameter-generate-file.component';

describe('DialogSearchParameterGenerateFileComponent', () => {
  let component: DialogSearchParameterGenerateFileComponent;
  let fixture: ComponentFixture<DialogSearchParameterGenerateFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchParameterGenerateFileComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchParameterGenerateFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
