import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyitinerariesComponent } from './myitineraries.component';

describe('MyitinerariesComponent', () => {
  let component: MyitinerariesComponent;
  let fixture: ComponentFixture<MyitinerariesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyitinerariesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyitinerariesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
