import { TestBed } from '@angular/core/testing';

import { UserMngService } from './userMng.service';

describe('UserMngService', () => {
  let service: UserMngService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserMngService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});