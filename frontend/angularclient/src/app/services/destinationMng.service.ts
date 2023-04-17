import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Page } from '../models/rest/page.model';
import { Destination } from '../models/destination.model';
import { InformationMngService } from './informationMng.service';

const baseUrl = '/api/management/destinations';

@Injectable({
  providedIn: 'root',
})
export class DestinationMngService implements InformationMngService {
  constructor(private httpClient: HttpClient) { }

  getList(page: number) {
    return this.httpClient.get(baseUrl + '?page=' + page).pipe() as Observable<
      Page<Destination>
    >;
  }

  createDestination(name: string, description: string, flagCode: string) {
    return this.httpClient.post(baseUrl, {
      name: name,
      description: description,
      flagCode: flagCode,
    });
  }

  editDestination(
    id: number,
    name: string,
    description: string,
    flagCode: string
  ) {
    return this.httpClient.put(baseUrl + '/' + id, {
      name: name,
      description: description,
      flagCode: flagCode,
    });
  }

  deleteDestination(id: number) {
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage() { }
}
