import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';


import { Page } from '../models/page.models';


const baseUrl = '/api/destinations';

@Injectable({
  providedIn: 'root'
})
export class DestinationService {

  constructor(private httpClient: HttpClient) { }

  getDestinations(): Observable<Page> {
 
    return this.httpClient.get<Page>(baseUrl)
  }

}
