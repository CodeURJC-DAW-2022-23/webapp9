import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { UserDetailsDTO } from '../models/rest/user-details-dto.model';
import { User } from '../models/user.model';
import { Itinerary } from '../models/itinerary.model';


const baseUrl = '/api/users/me';
const itiUrl = '/api/itineraries';
const BASE_URL = '/api/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  setUserImage(image: FormData) {
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
  
    return this.httpClient.put(baseUrl + '/image', image, { headers });
  }

  updateUserData(data: string) {
    console.log("updating user data");
    console.log("data looks like this:");
    console.log(JSON.parse(data));
    return this.httpClient.put(baseUrl, JSON.parse(data));
  }

  addUserItinerary(data: string) {
    console.log("updating new itinerary. data looks like this:");
    console.log(JSON.parse(data));
    return this.httpClient.post(itiUrl, JSON.parse(data));
  }

  editUserItinerary(id: number, data: string) {
    console.log("updating new itinerary. data looks like this:");
    console.log(JSON.parse(data));
    this.httpClient.put(itiUrl + "/" + id, JSON.parse(data)).subscribe({
      next: (response: any) => {
        console.log("response was:");
        console.log(response);
      },
      error: (err) => {
        console.error("Error when making new itinerary; " + JSON.stringify(err));
      }
    })
  }

  getMe(): Observable<UserDetailsDTO> {
    return this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me`, { withCredentials: true });
  }

  moreItineraries(page: number): Observable<UserDetailsDTO> {
    return this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me?pageItineraries=${page}`, { withCredentials: true });
  }

  moreReviews(page: number): Observable<UserDetailsDTO> {
    return this.httpClient.get<UserDetailsDTO>(`${BASE_URL}/me?pageReviews=${page}`, { withCredentials: true });
  }

	getImage(userId: number): string {
		return `${BASE_URL}/${userId}/image`;
	}

}
