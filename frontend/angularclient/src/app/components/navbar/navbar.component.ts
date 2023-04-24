import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { User } from 'src/app/models/user.model';
import { LogInService } from 'src/app/services/log-in.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  user!: User;
  name: string = "";

  @ViewChild('nameInput') nameInput!: ElementRef;
  constructor(public loginService: LogInService) { 
  }

  ngOnInit(){
    this.name = this.nameInput.nativeElement.value;
  }

  profileImage() {
    this.user = this.loginService.currentUser();
    return this.loginService.getImage(this.user);
  }

  search(f: NgForm) {
    window.location.href = `/search?name=${f.value.name}&type=itinerary&sort=id&order=DESC&page=0`;
  }

}
