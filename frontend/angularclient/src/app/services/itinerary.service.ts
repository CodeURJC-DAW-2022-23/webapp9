import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Itinerary } from '../models/itinerary.model';
import { Page } from '../models/rest/page.model';

import { InformationService } from './information.service';

const baseUrl = '/api/itineraries';

@Injectable({
  providedIn: 'root'
})
export class ItineraryService implements InformationService {

  constructor(private httpClient: HttpClient) { }

  getList(): Observable<Page<Itinerary>> {
    return this.httpClient.get<Page<Itinerary>>(baseUrl);
  }

  getItem(id: number): Observable<Itinerary> {
    return this.httpClient.get<Itinerary>(`${baseUrl}/${id}`);
  }

	getImage(itinerary: Itinerary): string {
		return itinerary.image ? `${baseUrl}/${itinerary.id}/image` : '/assets/images/no_image.png';
	}
  
  copy(id: number): Observable<Itinerary> {
    return this.httpClient.post(`${baseUrl}?copyFrom=${id}`, undefined) as Observable<Itinerary>;
	}

}
