import { Component, ElementRef, ViewChild } from '@angular/core';
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

  ngOnInit() {
    this.name = this.nameInput.nativeElement.value;
    this.loginService.reload;
  }

  profileImage() {
    this.user = this.loginService.currentUser();
    return this.loginService.getImage(this.user);
  }


}
