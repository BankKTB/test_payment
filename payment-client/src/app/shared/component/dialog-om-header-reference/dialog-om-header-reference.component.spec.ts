import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogOmHeaderReferenceComponent } from './dialog-om-header-reference.component';

describe('DialogOmHeaderReferenceComponent', () => {
  let component: DialogOmHeaderReferenceComponent;
  let fixture: ComponentFixture<DialogOmHeaderReferenceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogOmHeaderReferenceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogOmHeaderReferenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
