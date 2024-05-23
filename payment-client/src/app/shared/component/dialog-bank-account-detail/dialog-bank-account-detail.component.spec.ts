import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogBankAccountDetailComponent } from './dialog-bank-account-detail.component';

describe('DialogBankAccountDetailComponent', () => {
  let component: DialogBankAccountDetailComponent;
  let fixture: ComponentFixture<DialogBankAccountDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DialogBankAccountDetailComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogBankAccountDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
