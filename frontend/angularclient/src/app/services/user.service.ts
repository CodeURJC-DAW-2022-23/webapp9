import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDetailsDTO } from '../models/rest/user-details-dto.model';

const BASE_URL = '/api/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getMe(): Observable<UserDetailsDTO> {
    return this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me`, { withCredentials: true });
  }

  moreItineraries(page: number): Observable<UserDetailsDTO> {
    return this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me?pageItineraries=${page}`, { withCredentials: true });
  }

  moreReviews(page: number): Observable<UserDetailsDTO> {
    return this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me?pageReviews=${page}`, { withCredentials: true });
  }

}
