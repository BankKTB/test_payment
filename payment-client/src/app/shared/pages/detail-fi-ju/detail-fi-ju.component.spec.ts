import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailFiJuComponent } from './detail-fi-ju.component';

describe('DetailFiJuComponent', () => {
  let component: DetailFiJuComponent;
  let fixture: ComponentFixture<DetailFiJuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DetailFiJuComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailFiJuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
