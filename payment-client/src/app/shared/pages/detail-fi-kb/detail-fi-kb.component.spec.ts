import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailFiKbComponent } from './detail-fi-kb.component';

describe('DetailFiKbComponent', () => {
  let component: DetailFiKbComponent;
  let fixture: ComponentFixture<DetailFiKbComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DetailFiKbComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailFiKbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
