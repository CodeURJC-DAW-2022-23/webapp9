import { Component } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { LogInService } from 'src/app/services/log-in.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  user!: User;
  constructor(public loginService: LogInService) { 
  }

  profileImage() {
    this.user = this.loginService.currentUser();
    return this.loginService.getImage(this.user);
}

}
