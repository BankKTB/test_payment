import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmResultComponent } from './om-result.component';

describe('OmResultComponent', () => {
  let component: OmResultComponent;
  let fixture: ComponentFixture<OmResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmResultComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
