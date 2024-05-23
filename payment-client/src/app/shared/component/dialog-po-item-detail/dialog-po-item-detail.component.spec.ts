import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPoItemDetailComponent } from './dialog-po-item-detail.component';

describe('DialogPoItemDetailComponent', () => {
  let component: DialogPoItemDetailComponent;
  let fixture: ComponentFixture<DialogPoItemDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogPoItemDetailComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPoItemDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
