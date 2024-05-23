import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPoHeaderDetailComponent } from './dialog-po-header-detail.component';

describe('DialogPoHeaderDetailComponent', () => {
  let component: DialogPoHeaderDetailComponent;
  let fixture: ComponentFixture<DialogPoHeaderDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPoHeaderDetailComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPoHeaderDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
