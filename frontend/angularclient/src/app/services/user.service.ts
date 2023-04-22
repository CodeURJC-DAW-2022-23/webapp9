import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { User } from '../models/user.model';


const baseUrl = '/api/users/me';
const itiUrl = '/api/itineraries';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  setUserImage(image: FormData) {
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
  
    return this.httpClient.post(baseUrl + '/image', image, { headers });
  }

  updateUserData(data: string) {
    console.log("updating user data");
    console.log("data looks like this:");
    console.log(JSON.parse(data));
    this.httpClient.put(baseUrl, JSON.parse(data)).subscribe({
      next: (response: any) => {
        console.log("response was:");
        console.log(response);
      },
      error: (err) => {
        if (err.status != 404) {
          console.error('Error when asking if logged: ' + JSON.stringify(err));
        }
      }
    });
  }

  addUserItinerary(data: string): number {
    console.log("updating new itinerary. data looks like this:");
    console.log(JSON.parse(data));
    let val: number = -1;
    this.httpClient.post(itiUrl, JSON.parse(data)).subscribe({
      next: (response: any) => {
        console.log("response was:");
        console.log(response);
        val = response.id;
      },
      error: (err) => {
        console.error("Error when making new itinerary; " + JSON.stringify(err));
      }
    })

    return val;
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
}