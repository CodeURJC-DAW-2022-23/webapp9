import { TestBed } from '@angular/core/testing';

import { DestinationMngService } from './destinationMng.service';

describe('DestinationMngService', () => {
  let service: DestinationMngService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DestinationMngService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
