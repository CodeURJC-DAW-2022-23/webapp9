import { TestBed } from '@angular/core/testing';

import { ItineraryMngService } from './itineraryMng.service';

describe('ItineraryMngService', () => {
  let service: ItineraryMngService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItineraryMngService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
