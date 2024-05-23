import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DetailSngComponent } from './detail-sng.component';

describe('DetailSngComponent', () => {
  let component: DetailSngComponent;
  let fixture: ComponentFixture<DetailSngComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        declarations: [DetailSngComponent],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailSngComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
