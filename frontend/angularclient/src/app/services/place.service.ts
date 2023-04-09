import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Place } from '../models/place.model';
import { Page } from '../models/rest/page.model';

import { InformationService } from './information.service';

const baseUrl = '/api/place';

@Injectable({
  providedIn: 'root'
})
export class PlaceService implements InformationService {

  constructor(private httpClient: HttpClient) { }

  getList(): Observable<Page<Place>> {
    return this.httpClient.get<Page<Place>>(baseUrl);
  }

  getItem(id: number): Observable<Place> {
    return this.httpClient.get<Place>(baseUrl + "/" + id);
  }

}
