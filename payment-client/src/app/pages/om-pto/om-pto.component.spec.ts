import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmPtoComponent } from './om-pto.component';

describe('OmPtoComponent', () => {
  let component: OmPtoComponent;
  let fixture: ComponentFixture<OmPtoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmPtoComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmPtoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
