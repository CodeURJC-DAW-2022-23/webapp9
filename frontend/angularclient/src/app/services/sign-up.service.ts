import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';

const BASE_URL = '/api/users'

@Injectable({
  providedIn: 'root'
})
export class SignUpService {

  constructor(private httpClient: HttpClient) { }

  signUp(user: string, firstName: string, lastName:string, email:string, nationality:string, password:string){
    return this.httpClient.post(BASE_URL, { 'username': user, 'firstName': firstName, 'lastName':lastName, 'email∫':email, 'nationality':nationality, 'passwordHash':password});
  }

  getImage(user: User): string {
		return user.image ? `${BASE_URL}/${user.id}/image` : '/assets/images/no_image.png';
	}

  downloadImage(){
    return this.httpClient.post(BASE_URL + '/me/image', File)
  }
}
