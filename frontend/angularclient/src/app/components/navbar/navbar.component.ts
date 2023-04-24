import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NavigationStart, Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { LogInService } from 'src/app/services/log-in.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  user!: User | undefined;
  name: string = "";
  isSearch: boolean = false;

  @ViewChild('nameInput') nameInput!: ElementRef;
  
  constructor(private router: Router,
    public loginService: LogInService) {

    this.router.events.subscribe(
      (event) => {
        if (event instanceof NavigationStart) {
          this.isSearch = event.url.startsWith("/search")
        }
      });
  }

  ngOnInit() {
    this.name = this.nameInput.nativeElement.value;
    this.loginService.reload();
  }

  profileImage() {
    this.user = this.loginService.currentUser();
    if (this.user != undefined) return this.loginService.getImage(this.user);
    else return undefined;
  }

  search(f: NgForm) {
    window.location.href = `/search?name=${f.value.name}&type=itinerary&sort=id&order=DESC&page=0`;
  }

}
