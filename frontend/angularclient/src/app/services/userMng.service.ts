import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { User } from '../models/user.model';
import { Page } from '../models/rest/page.model';

const baseUrl = '/api/management/users';

@Injectable({
  providedIn: 'root',
})
export class UserMngService {
  constructor(private httpClient: HttpClient) { }

  getList(page: number) {
    return this.httpClient.get(baseUrl + '/?page=' + page).pipe() as Observable<
      Page<User>
    >;
  }

  getUser(id: number): Observable<User> {
    return this.httpClient.get<User>(baseUrl + '/' + id);
  }

  createUser(
    username: string,
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    nationality: string
  ): Observable<User> {
    return this.httpClient.post<User>(baseUrl, {
      username: username,
      firstName: firstName,
      lastName: lastName,
      email: email,
      passwordHash: password,
      nationality: nationality
    });
  }

  editUser(
    id: number,
    username: string,
    firstName: string,
    lastName: string,
    email: string,
    nationality: string
  ): Observable<User> {
    return this.httpClient.put<User>(baseUrl + '/' + id, {
      username: username,
      firstName: firstName,
      lastName: lastName,
      email: email,
      nationality: nationality
    });
  }

  deleteUser(id: number) {
    return this.httpClient.delete(baseUrl + '/' + id);
  }

  editImage(id: number, formData: FormData) {
    return this.httpClient.put(baseUrl + '/' + id + '/image', formData)
  }
}
