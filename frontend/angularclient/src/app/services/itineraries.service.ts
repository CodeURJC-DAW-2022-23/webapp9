import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../models/page.models';
import { Observable } from 'rxjs';
import { Itinerary } from '../models/itinerary.model';

const baseUrl = '/api/itineraries';

@Injectable({
  providedIn: 'root'
})
export class ItinerariesService {

  constructor(private httpClient:HttpClient) { }

  getItineraries(): Observable<Page> {
    return this.httpClient.get<Page>(baseUrl);
  }

  getImage(itinerary: Itinerary): string {
		return itinerary.image ? `${baseUrl}/${itinerary.id}/image` : '/assets/images/no_image.png';
	}
}
