import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailPoComponent } from './detail-po.component';

describe('DetailPoComponent', () => {
  let component: DetailPoComponent;
  let fixture: ComponentFixture<DetailPoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DetailPoComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailPoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
