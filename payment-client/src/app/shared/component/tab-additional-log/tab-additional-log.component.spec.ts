import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TabAdditionalLogComponent } from './tab-additional-log.component';

describe('TabAdditionalLogComponent', () => {
  let component: TabAdditionalLogComponent;
  let fixture: ComponentFixture<TabAdditionalLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TabAdditionalLogComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabAdditionalLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
