import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmCgdComponent } from './om-cgd.component';

describe('OmCgdComponent', () => {
  let component: OmCgdComponent;
  let fixture: ComponentFixture<OmCgdComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmCgdComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmCgdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
