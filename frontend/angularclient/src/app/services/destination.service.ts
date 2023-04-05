import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Page } from '../models/page.models';
import { Destination } from '../models/destination.model';

const baseUrl = '/api/destinations';

@Injectable({
  providedIn: 'root'
})
export class DestinationService {
  //private baseUrl = "https://localhost:8443/api/destinations"

  constructor(private httpClient: HttpClient) { }

  getDestinations(): Observable<Page> {
 
    return this.httpClient.get<Page>(baseUrl)
  }

}
