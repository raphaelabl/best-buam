import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BuffetDialogComponent} from './buffet-dialog.component';

describe('BuffetDialogComponent', () => {
  let component: BuffetDialogComponent;
  let fixture: ComponentFixture<BuffetDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuffetDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BuffetDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
