import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Itinerary } from '../models/itinerary.model';
import { Page } from '../models/rest/page.model';

import { InformationMngService } from './informationMng.service';

const baseUrl = '/api/management/itineraries';

@Injectable({
  providedIn: 'root',
})
export class ItineraryMngService implements InformationMngService {
  constructor(private httpClient: HttpClient) { }

  getList(page: number) {
    return this.httpClient.get(baseUrl + '/?page=' + page).pipe() as Observable<
      Page<Itinerary>
    >;
  }

  getItem(id: number): Observable<{itinerary: Itinerary}> {
    return this.httpClient.get<{itinerary: Itinerary}>('/api/itineraries/' + id);
  }

  createItem(
    name: string,
    description: string,
    username: string
  ) {
    return this.httpClient.post(baseUrl, {
      public: true,
      name: name,
      description: description,
      user: username,
    });
  }

  editItem(
    id: number,
    name: string,
    description: string,
    username: string
  ) {
    return this.httpClient.put(baseUrl + '/' + id, {
      public: true,
      name: name,
      description: description,
      username: username,
    });
  }

  deleteItem(id: number) {
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage() { }
}
