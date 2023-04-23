import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Itinerary } from '../models/itinerary.model';
import { Page } from '../models/rest/page.model';

import { InformationService } from './information.service';
import { Review } from '../models/review.model';

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
  
  loadMoreInformation(id: number, page: number = 0): Observable<Itinerary> {
    return this.httpClient.get<Itinerary>(`${baseUrl}/${id}?placesPage=${page}`);
  }
  
  loadMoreReviews(id: number, page: number = 0): Observable<Page<Review>> {
    return this.httpClient.get<Page<Review>>(`${baseUrl}/${id}/reviews?page=${page}`);
  }

  getPdfUrl(id: number) {
    return `${baseUrl}/${id}/pdf`
  }

  removePlace(itineraryId: number, placeId: number) {
    return this.httpClient.delete(`${baseUrl}/${itineraryId}/places/${placeId}`);
  }

  addPlace(itineraryId: number, placeId: number) {
    return this.httpClient.post(`${baseUrl}/${itineraryId}/places`, placeId);
  }

  addReview(id: number, review: { 
      title: string,
      description: string,
      score: number,
      user: string}) {

    return this.httpClient.post(`${baseUrl}/${id}/reviews`, review);
  }

}
