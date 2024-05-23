import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableReturnUpdateStatusComponent } from './table-return-update-status.component';

describe('TableReturnUpdateStatusComponent', () => {
  let component: TableReturnUpdateStatusComponent;
  let fixture: ComponentFixture<TableReturnUpdateStatusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TableReturnUpdateStatusComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableReturnUpdateStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
