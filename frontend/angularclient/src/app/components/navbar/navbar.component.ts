import { Component } from '@angular/core';
import { LogInService } from 'src/app/services/log-in.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(public loginService: LogInService) { }

}
