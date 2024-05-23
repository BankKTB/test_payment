import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TabIndependentComponent } from './tab-independent.component';

describe('TabIndependentComponent', () => {
  let component: TabIndependentComponent;
  let fixture: ComponentFixture<TabIndependentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TabIndependentComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabIndependentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
