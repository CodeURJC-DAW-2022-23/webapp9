import { Component } from '@angular/core';
import { LogInService } from 'src/app/services/log-in.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent {
  username!: string;
  password!: string;
  logInForm!: FormGroup

  constructor(private formBuilder: FormBuilder, public loginService: LogInService, public router: Router) { }

  ngOnInit(): void {
    this.logInForm = this.formBuilder.group({
      username:String,
      password: String
    })
  }


  logIn() {
    this.loginService.logIn(this.username, this.password).subscribe( data => {
      console.log(data);
      this.loginService.logged = true;
      this.router.navigateByUrl('');
    });
  }

  

}
