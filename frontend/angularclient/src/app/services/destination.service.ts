import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { Page } from '../models/rest/page.model';
import { Destination } from '../models/destination.model';
import { Graphic } from '../models/graphic.modul';

import { InformationService } from './information.service';

const baseUrl = '/api/destinations';

@Injectable({
  providedIn: 'root'
})
export class DestinationService implements InformationService {

  constructor(private httpClient: HttpClient) { }

  getList(): Observable<Page<Destination>> {
    return this.httpClient.get<Page<Destination>>(baseUrl);
  }

  getItem(id: number): Observable<Destination> {
    return this.httpClient.get<Destination>(`${baseUrl}/${id}`);
  }

  getDestinations(): Observable<Page<Destination>> {
    return this.httpClient.get<Page<Destination>>(baseUrl);
  }

  getChart():Observable<Graphic>{
    return this.httpClient.get<Graphic>('/api/graphs/index');
  }

	getImage(destination: Destination): string {
		return destination.image ? `${baseUrl}/${destination.id}/image` : '/assets/images/no_image.png';
	}
  
  loadMoreInformation(id: number, page: number = 0): Observable<Destination> {
    return this.httpClient.get<Destination>(`${baseUrl}/${id}?placesPage=${page}`);
  }


}
