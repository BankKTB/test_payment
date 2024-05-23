import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OmLogComponent } from './om-log.component';

describe('OmLogComponent', () => {
  let component: OmLogComponent;
  let fixture: ComponentFixture<OmLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OmLogComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OmLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
