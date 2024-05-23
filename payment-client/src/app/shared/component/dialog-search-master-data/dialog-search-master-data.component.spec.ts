import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSearchMasterDataComponent } from './dialog-search-master-data.component';

describe('DialogSearchMasterDataComponent', () => {
  let component: DialogSearchMasterDataComponent;
  let fixture: ComponentFixture<DialogSearchMasterDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogSearchMasterDataComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSearchMasterDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
