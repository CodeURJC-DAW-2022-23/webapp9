import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent {

  error!: string;

  constructor(private activatedRouter: ActivatedRoute) {
    activatedRouter.url.subscribe((data) => {
      this.error = data[1].path;
    });
  }

}
