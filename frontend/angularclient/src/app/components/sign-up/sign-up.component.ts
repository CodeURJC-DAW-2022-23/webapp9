import { Component, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { LogInService } from 'src/app/services/log-in.service';
import { SignUpService } from 'src/app/services/sign-up.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  username!: string;
  email!: string;
  password!: string;
  firstName!: string;
  lastName!: string;
  nationality!: string;
  image!: boolean
  signUpForm!: FormGroup

  @ViewChild('file') file: any;
  uploadedImage!: File;


  constructor(private formBuilder: FormBuilder, private router: Router, private service: SignUpService, private loginService: LogInService) {
  }

  ngOnInit(): void {
    this.signUpForm = this.formBuilder.group({
      username: String,
      password: String,
      firstName: String,
      lastName: String,
      email: String,
      nationality: String,
    })
  }

  signUp() {
    this.service.signUp(this.username, this.firstName, this.lastName, this.email, this.nationality, this.password).subscribe(
      data => {
        this.uploadImage(this.username, this.password);
      });
  }


  uploadImage(username: string, password: string): void {
    this.loginService.logIn(username, password).subscribe({
      next: () => {
        const image = this.file.nativeElement.files[0];
        if (image) {
          let formData = new FormData();
          formData.append("imageFile", image);
          this.service.downloadImage(formData).subscribe(
            { next: () => {
              this.logout();
              this.router.navigate(['/logIn'])
            },
              error: (error) => {
                if (error.status == 200) {
                  this.logout();
                  this.router.navigate(['/logIn']);
                } else {
                  this.logout();
                  this.router.navigate(['/error/', error.status])
                }
              }
            });
        }
      },

    });
  }

  logout() {

  }
}
