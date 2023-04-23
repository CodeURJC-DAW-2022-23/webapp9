import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { LogInService } from 'src/app/services/log-in.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{
  user: User | undefined;
  image: string | undefined;
  registered: boolean = false;
  isEditing: boolean = false;

  @ViewChild('userFile') userFile: any;

  constructor(private logInService: LogInService, private router: Router, private userService: UserService) {  }

  ngOnInit(): void {
    this.registered = this.logInService.isLogged();
    if (this.registered) {
      this.userService.getMe().subscribe({
        next: (response: any) => {
          this.user = response.user;
          if (this.user != undefined) this.image = this.logInService.getImage(this.user);
        },
        error: (err) => {
          if (err.status == 500) window.location.href = "/error/500";
          else window.location.href = "/error/403";
        }
      });
      if (this.user != undefined) this.image = this.logInService.getImage(this.user);
    }
  }

  afterViewInit(): void {
    this.registered = this.logInService.isLogged();
    if (this.registered) {
      this.userService.getMe().subscribe({
        next: (response: any) => {
          this.user = response.user;
          if (this.user != undefined) this.image = this.logInService.getImage(this.user);
        },
        error: (err) => {
          if (err.status == 500) window.location.href = "/error/500";
          else window.location.href = "/error/403";
        }
      });
      if (this.user != undefined) this.image = this.logInService.getImage(this.user);
    }
  }

  onLogout() {
    this.logout();
    this.router.navigate(['/']);
  }

  logout(): void {
    this.logInService.logOut();
  }

  onEditProfile() {
    this.isEditing = true;
  }

  onSubmit() {
    const firstName = (<HTMLInputElement>document.getElementById('form3Example1m')).value;
    const lastName = (<HTMLInputElement>document.getElementById('form3Example1n')).value;
    const username = (<HTMLInputElement>document.getElementById('form3Example8')).value;
    const nationality = (<HTMLInputElement>document.getElementById('form3Example10')).value;
    const email = (<HTMLInputElement>document.getElementById('form3Example9')).value;
    let logout = false;

    const updatedValues: any = {};

    if (this.user?.firstName !== firstName && firstName.trim() !== '') {
      updatedValues.firstName = firstName.trim();
    }

    if (this.user?.lastName !== lastName && lastName.trim() !== '') {
      updatedValues.lastName = lastName.trim();
    }

    if (this.user?.email !== email && email.trim() !== '') {
      updatedValues.email = email.trim();
    }

    if (this.user?.username !== username && username.trim() !== '') {
      updatedValues.username = username.trim();
      logout = true;
    }

    if (this.user?.nationality !== nationality && nationality.trim() !== '') {
      updatedValues.nationality = nationality.trim();
    }

    const img = this.userFile.nativeElement.files[0];
    if (img) {
      let formData = new FormData();
      formData.append("imageFile", img);
      this.userService.setUserImage(formData).subscribe();
    }

    if (this.logInService.isLogged()) this.userService.updateUserData(JSON.stringify(updatedValues));
    if (logout) {
      this.logout();
      this.router.navigate(['/login']);
    } else {
      this.isEditing = false;
      window.location.href = "/profile";
    }
  }
}