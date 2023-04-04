import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Destination } from '../models/destination.model';

const BASE_URL = "/api/destinations"



@Injectable({
  providedIn: 'root'
})
export class DestinationService {

  constructor(private httpClient: HttpClient) { }

  getDestinations(): Observable<Destination[]> {
    return this.httpClient.get(BASE_URL).pipe(
    ) as Observable<Destination[]>;
  }

}
