import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Observable } from 'rxjs';
import { Page } from '../models/page.models';


const BASE_URL = '/api/auth'

@Injectable({
  providedIn: 'root'
})
export class LogInService {

  public logged = false
  user!: User

  constructor(private httpClient: HttpClient) { 
    this.reqIsLogged();
  }


  logIn(user: string, pass: string) {
    return this.httpClient.post(BASE_URL + '/login', { 'username': user, 'password': pass })
  }


  reqIsLogged() {
    this.httpClient.get('/api/users/me', { responseType:"json" }).subscribe( {
      next: (response) => {
        this.user = response as User;
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
    /* this.reqIsLogged(); */
    return this.logged;
  } 
}
