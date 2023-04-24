import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEditMngComponent } from './add-edit-mng.component';

describe('AddEditMngComponent', () => {
  let component: AddEditMngComponent;
  let fixture: ComponentFixture<AddEditMngComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddEditMngComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddEditMngComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
