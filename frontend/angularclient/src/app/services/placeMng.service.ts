import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Place } from '../models/place.model';
import { Page } from '../models/rest/page.model';
import { InformationMngService } from './informationMng.service';

const baseUrl = '/api/management/places';

@Injectable({
  providedIn: 'root',
})
export class PlaceMngService implements InformationMngService {
  constructor(private httpClient: HttpClient) {}

  getList(page: number) {
    return this.httpClient.get(baseUrl + '/?page=' + page).pipe() as Observable<
      Page<Place>
    >;
  }

  getItem(id: number): Observable<{place: Place}> {
    return this.httpClient.get<{place: Place}>('/api/places/' + id);
  }

  createItem(name: string, description: string, destination: string) {
    return this.httpClient.post(baseUrl, {
      name: name,
      description: description,
      destination: destination,
    });
  }

  editItem(
    id: number,
    name?: string,
    description?: string,
    destination?: string
  ) {
    return this.httpClient.put(baseUrl + '/' + id, {
      name: name,
      description: description,
      destination: destination,
    });
  }

  deleteItem(id: number) {
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage() {}
}
