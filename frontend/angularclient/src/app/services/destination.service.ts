import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';


import { Page } from '../models/page.models';
import { Destination } from '../models/destination.model';
import { Graphic } from '../models/graphic.modul';




const baseUrl = '/api/destinations';

@Injectable({
  providedIn: 'root'
})
export class DestinationService {

  constructor(private httpClient: HttpClient) { }

  getDestinations(): Observable<Page> {
    return this.httpClient.get<Page>(baseUrl);
  }


  getChart():Observable<Graphic>{
    return this.httpClient.get<Graphic>('/api/graphs/index');
  }

  getImage(destination: Destination): string {
		return destination.image ? `${baseUrl}/${destination.id}/image` : '/assets/images/no_image.png';
	}


}
