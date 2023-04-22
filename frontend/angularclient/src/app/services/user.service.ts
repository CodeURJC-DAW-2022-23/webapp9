import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { User } from '../models/user.model';


const baseUrl = '/api/users/me';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  setUserImage(image: File) {
    const formData = new FormData();
    formData.append('imageFile', image, image.name);
    
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
  
    return this.httpClient.post(baseUrl + '/image', formData, { headers });
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
}
