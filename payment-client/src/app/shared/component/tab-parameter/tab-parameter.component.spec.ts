import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TabParameterComponent } from './tab-parameter.component';

describe('TabParameterComponent', () => {
  let component: TabParameterComponent;
  let fixture: ComponentFixture<TabParameterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TabParameterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
