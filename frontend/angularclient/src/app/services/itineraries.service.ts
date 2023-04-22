import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../models/page.models';
import { Observable } from 'rxjs';
import { Itinerary } from '../models/itinerary.model';

const baseUrl = '/api/itineraries';
const userItiUrl = '/api/users/me/itineraries';

@Injectable({
  providedIn: 'root'
})
export class ItinerariesService {

  constructor(private httpClient:HttpClient) { }

  getItineraries(): Observable<Page> {
    return this.httpClient.get<Page>(baseUrl);
  }

  deleteItineraryById(id: number) {
    return this.httpClient.delete(baseUrl + "/" + id);
  }

  getUserItineraries(): Observable<Page> {
    return this.httpClient.get<Page>(userItiUrl);
  }

  getImage(itinerary: Itinerary): string {
		return itinerary.image ? `${baseUrl}/${itinerary.id}/image` : '/assets/images/no_image.png';
	}

  setItineraryImage(id: number, data: FormData) {
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
  
    return this.httpClient.post(baseUrl + '/image', data, { headers });
  }
}
