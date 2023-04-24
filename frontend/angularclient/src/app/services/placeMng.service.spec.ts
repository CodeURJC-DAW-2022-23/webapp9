import { TestBed } from '@angular/core/testing';

import { PlaceMngService } from './placeMng.service';

describe('PlaceMngService', () => {
  let service: PlaceMngService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlaceMngService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
