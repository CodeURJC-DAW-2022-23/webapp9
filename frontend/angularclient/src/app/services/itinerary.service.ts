import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Itinerary } from '../models/itinerary.model';
import { Page } from '../models/rest/page.model';

import { InformationService } from './information.service';
import { Review } from '../models/review.model';

import { saveAs } from 'file-saver';

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
  
  loadMoreReviews(id: number, page: number = 0): Observable<Review[]> {
    return this.httpClient.get<Review[]>(`${baseUrl}/${id}/reviews?page=${page}`);
  }

  getPdfUrl(id: number) {
    return `${baseUrl}/${id}/pdf`
  }

  downloadPdf(id: number) {
    return this.httpClient.get<File>(`${baseUrl}/${id}/pdf`, { withCredentials: true }).subscribe({
       next: (file: File) => {
        var blob = new Blob([file], {type: "application/pdf;charset=utf-8"});
        saveAs(blob);
       }
    });
  }

}
