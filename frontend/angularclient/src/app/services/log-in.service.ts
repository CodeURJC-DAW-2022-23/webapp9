import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';


const BASE_URL = '/api/auth'

@Injectable({
  providedIn: 'root'
})
export class LogInService {

  public logged = false
  user: User | undefined;

  constructor(private httpClient: HttpClient) {
    this.reqIsLogged();
  }


  logIn(user: string, pass: string) {
    return  this.httpClient.post(BASE_URL + '/login', { 'username': user, 'password': pass })
  }


  reqIsLogged() {
    this.httpClient.get('/api/users/me', { responseType: "json" }).subscribe({
      next: (response: any) => {
        this.user = response.user as User;
        this.logged = true;
      },
      error: (err) => {
        if (err.status != 404) {
          console.error('Error when asking if logged: ' + JSON.stringify(err));
        }
      }
    });
  }


  isLogged() {
    return this.logged;
  }

  currentUser() {
    return this.user;
  }

  isAdmin() {
    if (this.user == undefined) return 
    else return this.user && this.user.roles.indexOf('ADMIN') !== -1;
  }

  getImage(user: User): string {
		return user.image ? `/api/users/${user.id}/image` : '/assets/images/no_image.png';
	}

  logOut() {
    return this.httpClient.post(BASE_URL + '/logout', { withCredentials: true })
        .subscribe((resp: any) => {
            console.log("LOGOUT: Successfully");
            this.logged = false;
            this.user = undefined;
      });
  }
  reload() {
    window.location.reload();
  }

}
