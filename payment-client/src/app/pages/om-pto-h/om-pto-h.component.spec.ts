import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmPtoHComponent } from './om-pto-h.component';

describe('OmPtoHComponent', () => {
  let component: OmPtoHComponent;
  let fixture: ComponentFixture<OmPtoHComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmPtoHComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmPtoHComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
