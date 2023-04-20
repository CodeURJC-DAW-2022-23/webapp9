import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Page } from '../models/rest/page.model';
import { Destination } from '../models/destination.model';
import { InformationMngService } from './informationMng.service';
import { Form } from '@angular/forms';

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

  getItem(id: number): Observable<{ destination: Destination }> {
    return this.httpClient.get<{ destination: Destination }>('/api/destinations/' + id);
  }

  createItem(name: string, description: string, flagCode: string): Observable<Destination> {
    return this.httpClient.post<Destination>(baseUrl, {
      name: name,
      description: description,
      flagCode: flagCode,
    });
  }

  editItem(
    id: number,
    name: string,
    description: string,
    flagCode: string
  ): Observable<Destination> {
    return this.httpClient.put<Destination>(baseUrl + '/' + id, {
      name: name,
      description: description,
      flagCode: flagCode,
    });
  }

  deleteItem(id: number) {
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage(id: number, formData: FormData) {
    return this.httpClient.put(baseUrl + '/' + id + '/image', formData)
   }
}
