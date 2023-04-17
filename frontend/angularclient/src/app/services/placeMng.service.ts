import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Place } from '../models/place.model';
import { Page } from '../models/rest/page.model';
import { InformationMngService } from './informationMng.service';

const baseUrl = '/api/management/place';

@Injectable({
  providedIn: 'root'
})
export class PlaceMngService implements InformationMngService{

  constructor(private httpClient: HttpClient) { }

  getList(): Observable<Page<Place>> {
    return this.httpClient.get<Page<Place>>(baseUrl);
  }

  createPlace(name: string, description: string, destination: string){
    return this.httpClient.post(baseUrl, {'name': name, 'description': description, 'destination': destination});
  }

  editPlace(id: number, name: string, description: string, destination: string){
    return this.httpClient.put(baseUrl + '/' + id, {'name': name, 'description': description, 'destination': destination});
  }

  deletePlace(id: number){
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage(){

  }


}
