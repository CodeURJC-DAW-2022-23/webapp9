import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../models/page.models';
import { Place } from '../models/place.model';

const baseUrl = '/api/places';

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private httpClient:HttpClient) { }

  getPlaces(): Observable<Page> { 
    return this.httpClient.get<Page>(baseUrl)
  }

  getImage(place: Place): string {
		return place.image ? `${baseUrl}/${place.id}/image` : '/assets/images/no_image.png';
	}
}
