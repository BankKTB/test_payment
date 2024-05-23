import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchMasterComponent } from './dialog-search-master.component';

describe('DialogSearchMasterComponent', () => {
  let component: DialogSearchMasterComponent;
  let fixture: ComponentFixture<DialogSearchMasterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchMasterComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchMasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
