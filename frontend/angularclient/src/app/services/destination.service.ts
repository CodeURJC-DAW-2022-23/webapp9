import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Destination } from '../models/destination.model';


@Injectable({
  providedIn: 'root'
})
export class DestinationService {
  private baseUrl = "https://localhost:8443/api/destinations/"

  constructor(private httpClient: HttpClient) { }

  getDestinations(): Observable<Destination[]> {
 
    return this.httpClient.get<Destination[]>(`${this.baseUrl}`);
  }

}
