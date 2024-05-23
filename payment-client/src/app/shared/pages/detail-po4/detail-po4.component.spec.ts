import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailPo4Component } from './detail-po4.component';

describe('DetailPo4Component', () => {
  let component: DetailPo4Component;
  let fixture: ComponentFixture<DetailPo4Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DetailPo4Component],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailPo4Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
