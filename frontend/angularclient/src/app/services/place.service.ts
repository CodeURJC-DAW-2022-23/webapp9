import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Place } from '../models/place.model';
import { Page } from '../models/rest/page.model';

import { InformationService } from './information.service';

const baseUrl = '/api/places';

@Injectable({
  providedIn: 'root'
})
export class PlaceService implements InformationService {

  constructor(private httpClient: HttpClient) { }

  search(name: string, type: string, sort: string, order: string, page: number): Observable<Page<Place>> {
    let params = new HttpParams();
    params = params.append('name', name);
    params = params.append('type', type);
    params = params.append('sort', sort);
    params = params.append('order', order);
    params = params.append('page', page);

    return this.httpClient.get<Page<Place>>(baseUrl, {params: params});
  }

  getList(): Observable<Page<Place>> {
    return this.httpClient.get<Page<Place>>(baseUrl);
  }

  getItem(id: number): Observable<Place> {
    return this.httpClient.get<Place>(`${baseUrl}/${id}`);
  }

	getImage(place: Place): string {
		return place.image ? `${baseUrl}/${place.id}/image` : '/assets/images/no_image.png';
	}
  
  loadMoreInformation(id: number, page: number = 0): Observable<Place> {
    return this.httpClient.get<Place>(`${baseUrl}/${id}?page=${page}`);
  }

}
