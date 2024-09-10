import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartyAdminComponent } from './party-admin.component';

describe('PartyAdminComponent', () => {
  let component: PartyAdminComponent;
  let fixture: ComponentFixture<PartyAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartyAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartyAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
