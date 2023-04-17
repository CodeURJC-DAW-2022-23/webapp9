import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { User } from '../models/user.model';
import { Page } from '../models/rest/page.model';
import { InformationMngService } from './informationMng.service';

const baseUrl = '/api/management/user';

@Injectable({
  providedIn: 'root'
})
export class UserMngService implements InformationMngService{

  constructor(private httpClient: HttpClient) { }

  getList(): Observable<Page<User>> {
    return this.httpClient.get<Page<User>>(baseUrl);
  }

  createUser(username: string, firstName: string, lastName: string, email: string, password: string){
    return this.httpClient.post(baseUrl, {'username': username, 'firstName': firstName, 'lastName': lastName, 'email': email, 'password': password});
  }

  editUser(id: number, username: string, firstName: string, lastName: string, email: string){
    return this.httpClient.put(baseUrl + '/' + id, {'username': username, 'firstName': firstName, 'lastName': lastName, 'email': email});
  }

  deleteUser(id: number){
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage(){

  }

}
