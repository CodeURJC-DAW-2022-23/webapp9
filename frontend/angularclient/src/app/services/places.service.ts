import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../models/page.models';
const baseUrl = '/api/places';

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private httpClient:HttpClient) { }

  getPlaces(): Observable<Page> {
 
    return this.httpClient.get<Page>(baseUrl)
  }
}
