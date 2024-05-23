import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmResultEComponent } from './om-result-e.component';

describe('OmResultEComponent', () => {
  let component: OmResultEComponent;
  let fixture: ComponentFixture<OmResultEComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmResultEComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmResultEComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
