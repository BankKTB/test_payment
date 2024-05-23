import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InitFromIamComponent } from './init-from-iam.component';

describe('InitFromIamComponent', () => {
  let component: InitFromIamComponent;
  let fixture: ComponentFixture<InitFromIamComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [InitFromIamComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InitFromIamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
