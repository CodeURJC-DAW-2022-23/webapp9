import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NavigationSkipped, NavigationStart, Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { LogInService } from 'src/app/services/log-in.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  user?: User;
  name: string = "";
  isSearch: boolean = false;
  image!: string;
  admin: boolean = false;
  error: boolean = false;

  @ViewChild('nameInput') nameInput!: ElementRef;

  constructor(private router: Router,
    public loginService: LogInService,
    public userService: UserService) {

    this.router.events.subscribe(
      (event) => {
        if (event instanceof NavigationSkipped && event.url.startsWith("/profile")) {
          this.userService.getMe().subscribe(
            (data) => {
              this.user = data.user;
              this.image = this.userService.getImage(this.user.id);
              this.admin = this.user.roles.indexOf('ADMIN') !== -1;
            }
          )
        } else if (event instanceof NavigationStart) {
          this.isSearch = event.url.startsWith("/search");
          this.error = event.url.startsWith("/error");
          this.userService.getMe().subscribe(
            (data) => {
              this.user = data.user;
              this.image = this.userService.getImage(this.user.id);
              this.admin = this.user.roles.indexOf('ADMIN') !== -1;
            }
          )
        }
      });
  }

  ngOnInit() {
    this.name = this.nameInput.nativeElement.value;
    if ((this.user = this.loginService.currentUser()) != undefined) this.image = this.userService.getImage(this.user.id);
    this.loginService.reload();
  }

  profileImage() {
    if (this.loginService.currentUser() != undefined) {
      this.user = this.loginService.currentUser();
      if (this.user != undefined) return this.loginService.getImage(this.user);
    }
    return undefined;
  }

  search(f: NgForm) {
    this.router.navigate(["/search"], { queryParams: { name: f.value.name, type: "itinerary", sort: "id", order: "DESC", page: 0 } });
  }

}
