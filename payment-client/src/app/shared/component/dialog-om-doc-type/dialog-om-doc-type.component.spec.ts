import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmDocTypeComponent } from './dialog-om-doc-type.component';

describe('DialogOmDocTypeComponent', () => {
  let component: DialogOmDocTypeComponent;
  let fixture: ComponentFixture<DialogOmDocTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogOmDocTypeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmDocTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
