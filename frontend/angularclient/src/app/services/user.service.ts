import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserDetailsDTO } from '../models/rest/user-details-dto.model';
import { UserDetails } from '../models/rest/user-details.model';

const BASE_URL = '/api/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getMe(): UserDetails {
    let user: UserDetails;
    this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me`, { withCredentials: true }).subscribe((response) => {
      let pages: number = Math.max(
          (response.itineraries.numberOfElements % 10 > 0 ? 1 : 0) + Math.floor(response.itineraries.numberOfElements / 10) + 1,
          (response.reviews.numberOfElements % 10 > 0 ? 1 : 0) + Math.floor(response.reviews.numberOfElements / 10) + 1)

      user = {
        user: response.user,
        itineraries: response.itineraries.content,
        reviews: response.reviews.content
      }

      for (let page = 0; page < pages; page++) {
        this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me?itineraryPage=${page}&reviewPage=${page}`, { responseType: "json" }).subscribe((response) => {
          response.itineraries.content.forEach((i) => {user.itineraries.push(i)});
          response.reviews.content.forEach((r) => {user.reviews.push(r)});
        });
      }
      return user;
    });
  }

}
